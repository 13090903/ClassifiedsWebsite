package ru.vsu.csf.classifiedsweb.util.pairs;

import lombok.Getter;
import lombok.Setter;
import ru.vsu.csf.classifiedsweb.models.User;


@Getter
@Setter
public class UserPlace {
    public User user;
    public Long place;

    public UserPlace(User user, Long place) {
        this.user = user;
        this.place = place;
    }
}
