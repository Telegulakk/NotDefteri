package JavaFx.model;

import java.time.LocalDateTime;

public class Note {
    public int id;
    public String title;
    public String content;
    public LocalDateTime creationDate;
    public LocalDateTime replacementDate;

    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationDate = LocalDateTime.now();
        this.replacementDate = LocalDateTime.now();
    }
}
