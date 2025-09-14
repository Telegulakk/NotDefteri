// dosya: view/NotEditorView.java
package JavaFx.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NotEditorView extends VBox {
    private TextField titleField = new TextField();
    private TextArea contentArea = new TextArea();
    private Button closeButton = new Button("X");
    private Button deleteButton = new Button("Sil");

    public NotEditorView() {
        this.setSpacing(10);
        this.setPadding(new Insets(10, 10, 10, 10)); // Kenarlardan boşluk ekler
        this.setPrefWidth(10);

        // Ekran büyüdükçe TextArea'nın da büyümesini sağla
        VBox.setVgrow(contentArea, Priority.ALWAYS); // dikeyde nasıl büyüyeceğini veya ekstra dikey alanı nasıl dolduracağını belirten
        contentArea.setWrapText(true); // Opsiyonel: içerik satıra sığmadığında otomatik sar

        //css
        titleField.getStyleClass().add("transparent-text-field");
        contentArea.getStyleClass().add("transparent-text-area");

        this.getChildren().addAll(closeButton, deleteButton, titleField, contentArea);
    }

    public TextField getTitleField() {
        return titleField;
    }

    public TextArea getContentArea() {
        return contentArea;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }


}
