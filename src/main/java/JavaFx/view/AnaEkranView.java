// dosya: view/AnaEkranView.java
package JavaFx.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AnaEkranView extends VBox {


    private VBox anaEkran = new VBox(10);
    private Button ekleButton = new Button("+");
    public Button siralamaButton = new Button(aktifSiralamaOku());

    private static final String SIRALAMA_DOSYASI = "siralama_ayarlari.json";
    private static final String VARSAYILAN_SIRALAMA = "DEGISTIRME_TARIHI";

    private String aktifSiralamaOku() {
        try {
            String jsonText = Files.readString(Path.of(SIRALAMA_DOSYASI));
            JSONObject obj = new JSONObject(jsonText);
            return obj.getString("aktif_siralama");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return VARSAYILAN_SIRALAMA;
        }
    }

    public AnaEkranView() {
        siralamaButton.setFont(new Font(15));
        ekleButton.setFont(new Font(20)); // buton üzerindeki yazı boyutu
        ekleButton.setPrefWidth(50); //  tercih edilen genişlik
        anaEkran.setPadding(new Insets(10)); // bileşenin iç kenarları ile içeriği arasına 10 birim boşluk (padding) ekler. Insets(10) tüm kenarlardan eşit boşluk bırakır
        anaEkran.getStyleClass().add("ana-ekran"); // css ile görselleştirme
        // veya bunu kullan anaEkran.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setSpacing(10); // bileşenler arası boşluk
        this.getChildren().addAll(ekleButton, siralamaButton, anaEkran);
    }

    public Button getEkleButton() {
        return ekleButton;
    }

    public Button getSiralamaButton() {
        return siralamaButton;
    }

    public VBox getAnaEkran() {
        return anaEkran;
    }
}
