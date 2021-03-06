package aia.com.wheely_map.user;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;
import java.util.List;

public class User {

    private static final String TAG = User.class.getSimpleName();

    private final String USER_ID;
    private String username;
    private long userPoints;

    public User(GoogleSignInAccount user) {
        username = user.getDisplayName();
        USER_ID = user.getId();
        this.userPoints = 0;
    }

    public User() {
        username = "TEST";
        USER_ID = "TEST";
    }

    public void setUserPoints(long points) {
        this.userPoints = points;
    }

    public boolean addUserPoints(long points) {
        if (points < 0) {
            negativePointsError(points);
            return false;
        }
        this.userPoints += points;
        return true;
    }

    public boolean removeUserPoints(long points) {
        if (points < 0 || userPoints - points < 0) {
            negativePointsError(points);
            return false;
        }
        this.userPoints -= points;
        return true;
    }

    public String getUsername() {
        return username;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public long getUserPoints() {
        return userPoints;
    }

    private static void negativePointsError(long points) {
        Log.e(TAG, "Attempted to use negative points " + points);
    }
}
