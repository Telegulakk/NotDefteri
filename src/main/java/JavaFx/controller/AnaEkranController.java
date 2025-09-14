package JavaFx.controller;

import JavaFx.model.Note;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import JavaFx.view.AnaEkranView;
import JavaFx.view.NotEditorView;

import java.io.File;

import JavaFx.controller.NotEditorController.SiralamaTuru;

import java.util.*;

public class AnaEkranController {

    private final AnaEkranView anaEkranView;
    private final NotEditorView notEditorView;
    private final NotEditorController noteEditorController;
    private final BorderPane rootLayout;
    private Button oncekiNotButonu = null;
    private Stage primaryStage;
    public static Map<Integer, Button> buttons = new HashMap<>();


    public AnaEkranController(AnaEkranView anaEkranView, NotEditorView notEditorView,
                              NotEditorController noteController, BorderPane rootLayout, Stage primaryStage) {
        this.anaEkranView = anaEkranView;
        this.notEditorView = notEditorView;
        this.noteEditorController = noteController;
        this.rootLayout = rootLayout;
        this.primaryStage = primaryStage;

        initialize();
    }

    private void initialize() {
        NotEditorController.SiralamaTuru.ayarlariOku();
        noteEditorController.dosyalardanNotlariYukle();

        for (Map.Entry<Integer, File> entry : noteEditorController.files.entrySet()) {  // files mapindeki tüm file'lari döndürüyor
            Integer key = entry.getKey();   // mevcut Key'e erişim
            Note not = noteEditorController.getNote(key);  // mevcut Not'a erişim
            Button notButonu = new Button(not.title);

            notButonu.getStyleClass().add("note-button"); /*css yerine notButonu.setMinWidth(60); notButonu.setMinHeight(10); yazılabilir*/
            notButonu.setUserData(key);
            anaEkranView.getAnaEkran().getChildren().add(notButonu);

            baglaNotButonu(notButonu); // not aksiyonları
        }

        handleCloseRequest(); // direkt kapatınca kaydetme olmasını sağlayan metod

        anaEkranView.getEkleButton().setOnAction(e -> {
            Note yeniNot = noteEditorController.yeniNotOlustur();
            Button notButonu = new Button("Başlık " + yeniNot.id);
            notButonu.getStyleClass().add("note-button"); /*css yerine notButonu.setMinWidth(60); notButonu.setMinHeight(10); yazılabilir*/
            notButonu.setUserData(yeniNot.id);

            anaEkranView.getAnaEkran().getChildren().add(notButonu);
            baglaNotButonu(notButonu); // not aksiyonları

        });

        anaEkranView.getSiralamaButton().setOnAction(e -> {
            int siralamaID = (SiralamaTuru.ayarlariOku().getDeger() + 1) % 3; // sonraki sıralamaya geçmesi için
            SiralamaTuru yeniSiralamaTuru = SiralamaTuru.getByDeger(siralamaID);  // sonraki SiralamaTuru (örn. OLUSTURMA_TARIHI)

            String yeniSiralama = SiralamaTuru.getByDeger(siralamaID).getDisplayName(); // örn. OLUSTURMA_TARIHI nin displayname'i --> Oluşturma Tarihi
            anaEkranView.siralamaButton.setText(yeniSiralama); // buton adı değişiyor

            SiralamaTuru.ayarlariKaydet(yeniSiralamaTuru); //  sonraki SiralamaTurunu (örn. OLUSTURMA_TARIHI) jsona kaydediyoruz

            switch (siralamaID) {
                case 0: // degistirme
                    System.out.println("DEGISTIRME_TARIHI");
                    break;
                case 1: // olusturma
                    System.out.println("OLUSTURMA_TARIHI");
                    olusturmaTarihineGoreSiralama();
                    break;
                case 2: // ad
                    adaGoreSiralama();
                    System.out.println("AD");
                    break;
            }
        });
    }

    public void olusturmaTarihineGoreSiralama() {
        for (Map.Entry<Integer, File> entry : noteEditorController.files.entrySet()) {  // files mapindeki tüm file'lari döndürüyor
            Integer key = entry.getKey();   // mevcut Key'e erişim
            Note not = noteEditorController.getNote(key);  // mevcut Not'a erişim

            Button notButonu = new Button(not.title);

            notButonu.getStyleClass().add("note-button"); /*css yerine notButonu.setMinWidth(60); notButonu.setMinHeight(10); yazılabilir*/
            notButonu.setUserData(key);
            anaEkranView.getAnaEkran().getChildren().add(notButonu);

            baglaNotButonu(notButonu); // not aksiyonları
        }
    }

    public void degistirmeTarihineGoreSiralama() {
        List<Note> noteList = new ArrayList<>();
        for (Map.Entry<Integer, File> entry : noteEditorController.files.entrySet()) {
            Integer key = entry.getKey();
            Note not = noteEditorController.getNote(key);
            noteList.add(not);
        }

        noteList.sort(Comparator.comparing(note -> note.title));

        for (Note not : noteList) {
            Integer key = not.id; // doğrudan erişim

            Button notButonu = new Button(not.title);
            notButonu.setUserData(key);

            notButonu.getStyleClass().add("note-button");
            anaEkranView.getAnaEkran().getChildren().add(notButonu);
            baglaNotButonu(notButonu);
        }

    }

    public void adaGoreSiralama() {
        List<Note> noteList = new ArrayList<>();
        for (Map.Entry<Integer, File> entry : noteEditorController.files.entrySet()) {
            Integer key = entry.getKey();
            Note not = noteEditorController.getNote(key);
            noteList.add(not);
        }
        noteList.sort(Comparator.comparing(note -> note.title));

        for (Note not : noteList) {
            Integer key = not.id; // doğrudan erişim

            Button notButonu = new Button(not.title);
            notButonu.setUserData(key);

            notButonu.getStyleClass().add("note-button");
            anaEkranView.getAnaEkran().getChildren().add(notButonu);
            baglaNotButonu(notButonu);
        }
    }


    private void baglaNotButonu(Button notButonu) {
        notButonu.setOnAction(ev -> {
            // Önceki notu güncelle
            if (noteEditorController.aktifId != -1 && noteEditorController.oncekiId != -1) {
                oncekiNotButonu.setText(notEditorView.getTitleField().getText());
                noteEditorController.guncelleNote(
                        noteEditorController.aktifId,
                        notEditorView.getTitleField().getText(),
                        notEditorView.getContentArea().getText()
                );
            }

            // Seçili notu yükle
            int myId = (int) notButonu.getUserData();
            Note seciliNot = noteEditorController.getNote(myId);
            noteEditorController.aktifId = myId;
            noteEditorController.oncekiId = noteEditorController.aktifId;
            oncekiNotButonu = notButonu;

            notEditorView.getTitleField().setText(seciliNot.title);
            notEditorView.getContentArea().setText(seciliNot.content);
            rootLayout.setCenter(notEditorView);

            // Silme
            notEditorView.getDeleteButton().setOnAction(del -> {
                noteEditorController.guncelleNote(myId,
                        notEditorView.getTitleField().getText(),
                        notEditorView.getContentArea().getText());

                anaEkranView.getAnaEkran().getChildren().remove(notButonu);
                noteEditorController.silNote(myId);
                rootLayout.setCenter(null);
            });

            // Kapat
            notEditorView.getCloseButton().setOnAction(close -> {
                noteEditorController.guncelleNote(myId,
                        notEditorView.getTitleField().getText(),
                        notEditorView.getContentArea().getText());

                notButonu.setText(notEditorView.getTitleField().getText());
                rootLayout.setCenter(null);
            });
        });
    }

    private void handleCloseRequest() {
        primaryStage.setOnCloseRequest(event -> {
            if (noteEditorController.aktifId != -1) {
                noteEditorController.guncelleNote(
                        noteEditorController.aktifId,
                        notEditorView.getTitleField().getText(),
                        notEditorView.getContentArea().getText()
                );
            }
        });
    }
}
