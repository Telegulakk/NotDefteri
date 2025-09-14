package JavaFx.controller;

import JavaFx.model.Note;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NotEditorController {
    public static Map<Integer, Note> notes = new HashMap<>();
    public static Map<Integer, File> files = new HashMap<>();

    private AtomicInteger globalId = new AtomicInteger(0);
    int aktifId = -1;
    int oncekiId = -1;

    public String dosyaAdi(int id) {
        return id + ".json";
    }

    public Note yeniNotOlustur() {
        int id = globalId.getAndIncrement();
        Note note = new Note(id, "", "");
        notes.put(id, note);

        File dosya = new File(dosyaAdi(id));
        files.put(id, dosya);

        kaydetNot(note); // notu dosyaya kaydet

        return note;
    }


    private void kaydetNot(Note note) {
        JSONObject j = new JSONObject();
        j.put("id", note.id);
        j.put("title", note.title);
        j.put("content", note.content);
        j.put("creation date", note.creationDate.toString());
        j.put("replacement date", note.replacementDate.toString());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(note.id)))) {
            writer.write(j.toString(4)); // girintili yaz
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guncelleNote(int id, String yeniBaslik, String yeniIcerik) {
        Note n = notes.get(id);
        if (n != null) {
            n.title = yeniBaslik;
            n.content = yeniIcerik;
            n.replacementDate = LocalDateTime.now();

            kaydetNot(n); // JSON dosyasını güncelle
        }
    }

    public void silNote(int id) {
        notes.remove(id);
        files.get(id).delete();
        files.remove(id);
    }

    public Note getNote(int id) {
        return notes.get(id);
    }

    public void dosyalardanNotlariYukle() {
        File klasor = new File(".");
        File[] dosyalar = klasor.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("siralama_ayarlari.json")); // dir, file dosya yolu;name, string dosya adı

        if (dosyalar == null) return;

        for (File f : dosyalar) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String jsonText = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject j = new JSONObject(jsonText);

                int id = j.getInt("id");
                String title = j.getString("title");
                String content = j.getString("content");
                LocalDateTime creation = LocalDateTime.parse(j.getString("creation date"));
                LocalDateTime replacement = LocalDateTime.parse(j.getString("replacement date"));

                Note n = new Note(id, title, content);
                n.creationDate = creation;
                n.replacementDate = replacement;

                notes.put(id, n);
                files.put(id, f);

                if (id >= globalId.get()) {
                    globalId.set(id + 1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void degistirmeTarihineGoreSirala() {
        File klasor = new File(".");
        File[] dosyalar = klasor.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("siralama_ayarlari.json")); // dir, file dosya yolu;name, string dosya adı

        // 1. Dosyaları replacement date'e göre sıralamak için liste hazırla


        for (File f : dosyalar) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String jsonText = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject j = new JSONObject(jsonText);

                int id = j.getInt("id");
                String title = j.getString("title");
                String content = j.getString("content");
                LocalDateTime creation = LocalDateTime.parse(j.getString("creation date"));
                LocalDateTime replacement = LocalDateTime.parse(j.getString("replacement date"));



            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (dosyalar == null) return;

        for (File f : dosyalar) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String jsonText = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject j = new JSONObject(jsonText);

                int id = j.getInt("id");
                String title = j.getString("title");
                String content = j.getString("content");
                LocalDateTime creation = LocalDateTime.parse(j.getString("creation date"));
                LocalDateTime replacement = LocalDateTime.parse(j.getString("replacement date"));

                Note n = new Note(id, title, content);
                n.creationDate = creation;
                n.replacementDate = replacement;

                notes.put(id, n);
                files.put(id, f);

                if (id >= globalId.get()) {
                    globalId.set(id + 1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public enum SiralamaTuru {
        DEGISTIRME_TARIHI("Değiştirme Tarihi", 0),
        OLUSTURMA_TARIHI("Oluşturma Tarihi", 1),
        AD("Ad", 2);

        private final String displayName;
        private final int deger;

        // Constructor
        SiralamaTuru(String displayName, int deger) {
            this.displayName = displayName;
            this.deger = deger;
        }

        public static SiralamaTuru getByDeger(int deger) {
            for (SiralamaTuru tip : values()) {
                if (tip.getDeger() == deger) {
                    return tip;
                }
            }
            throw new IllegalArgumentException("Geçersiz değer: " + deger);
        }

        // Getter'lar
        public String getDisplayName() {
            return displayName;
        }

        public int getDeger() {
            return deger;
        }

        // Ayarları kaydetme
        public static void ayarlariKaydet(SiralamaTuru aktifSiralama) {
            File ayarlarFile = new File("siralama_ayarlari.json");
            JSONObject ayarlar = new JSONObject();

            ayarlar.put("aktif_siralama", aktifSiralama.name()); // DEGISTIRME_TARIHI gibi

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ayarlarFile))) {
                writer.write(ayarlar.toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Ayarları okuma
        public static SiralamaTuru ayarlariOku() {
            File ayarlarFile = new File("siralama_ayarlari.json");

            if (!ayarlarFile.exists()) {
                // Varsayılan ayar
                return SiralamaTuru.DEGISTIRME_TARIHI;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(ayarlarFile))) {
                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }

                JSONObject ayarlar = new JSONObject(jsonString.toString());
                String aktifSiralama = ayarlar.getString("aktif_siralama");

                return SiralamaTuru.valueOf(aktifSiralama);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return SiralamaTuru.DEGISTIRME_TARIHI; // Hata durumunda varsayılan
            }
        }
    }
}
