package org.botty.permissionshandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AuthorizedUsersManager {

    private Set<String> allowedUserIds;

    public AuthorizedUsersManager() {
        loadAuthorizedUserIds();
        printAuthorizedUserIds();
    }

    public void refreshAuthorizedUserIds() {
        loadAuthorizedUserIds();
        printAuthorizedUserIds();
    }

    public boolean isAuthorized(String userId) {
        return allowedUserIds.contains(userId);
    }

    public Set<String> getAllowedUserIds() {
        return allowedUserIds;
    }

    public void addUser(String newUserId) {
        allowedUserIds.add(newUserId);
        updateJsonFile();
    }

    private void updateJsonFile() {
        try {
            File file = new File("authorized_users.json");
            FileWriter writer = new FileWriter(file);
            JSONObject json = new JSONObject();
            JSONArray userIdArray = new JSONArray();

            for (String userId : allowedUserIds) {
                userIdArray.put(userId);
            }

            json.put("authorizedUserIds", userIdArray);
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadAuthorizedUserIds() {
        allowedUserIds = new HashSet<>();

        try {
            File file = new File("authorized_users.json");

            if (!file.exists()) {
                // If the file doesn't exist, create it with some default authorized user IDs
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                JSONObject json = new JSONObject();
                JSONArray defaultUserIds = new JSONArray();
                defaultUserIds.put("DEFAULT_USER_ID_1");
                defaultUserIds.put("DEFAULT_USER_ID_2");
                json.put("authorizedUserIds", defaultUserIds);
                writer.write(json.toString());
                writer.close();
            }

            FileReader reader = new FileReader(file);
            JSONObject json = new JSONObject(new JSONTokener(reader));
            JSONArray userArray = json.getJSONArray("authorizedUserIds");

            for (int i = 0; i < userArray.length(); i++) {
                allowedUserIds.add(userArray.getString(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printAuthorizedUserIds() {
        System.out.println("Authorized User IDs:");

        for (String userId : allowedUserIds) {
            System.out.println("- " + userId);
        }
    }
}
