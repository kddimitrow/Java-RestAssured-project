package AllUserCases;

import java.util.List;

public class Thread {


    private int type_id;
    private String title;
    private String body;
    private int is_anonymous;
    private List<String> attachments;


    public Thread(int type_id, String title, String body, int is_anonymous) {
        this.type_id = type_id;
        this.title = title;
        this.body = body;
        this.is_anonymous = is_anonymous;
    }

    public Thread(int type_id, String title, String body, List<String> attachments) {
        this.type_id = type_id;
        this.title = title;
        this.body = body;
        this.attachments = attachments;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public int getType_id() {
        return type_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getIs_anonymous() {
        return is_anonymous;
    }


}
