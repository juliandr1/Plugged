package com.example.reshoe_fbu.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String TAG = "User";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_IS_SELLER = "isSeller";
    public static final String KEY_PROFILE_PIC = "profilePic";

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public void setPassword(String password) { put(KEY_PASSWORD, password); }

    public String getEmail() {
        return getString(KEY_EMAIL);
    }

    public void setEmail(String email) {
        put (KEY_EMAIL, email);
    }

    public void setFirstName(String firstName) { put (KEY_FIRST_NAME, firstName); }

    public void setLastName(String lastName) { put (KEY_LAST_NAME, lastName); }

    public boolean getIsSeller() { return getBoolean(KEY_IS_SELLER); }

    public void setIsSeller(Boolean isSeller) { put (KEY_IS_SELLER, isSeller); }

    public ParseFile getProfilePic() { return getParseFile(KEY_PROFILE_PIC); }

    public void setProfilePic(ParseFile file) { put (KEY_IS_SELLER, file); }

}
