package shopinpager.wingstud.shopinpagerseller.model;

import java.io.Serializable;

/**
 * Created by mukesh verma on 13-09-2019.
 */

public class NoticeModel implements Serializable {

    String id, heading, description, created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}