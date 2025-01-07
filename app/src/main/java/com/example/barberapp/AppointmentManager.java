package com.example.barberapp;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentManager {

    /**
     * מחשב את רשימת השעות הפנויות על בסיס רשימת התורים הקיימים ורשימת כל השעות ביום.
     *
     * @param appointments נתונים מ-Firebase שמכילים את התורים הקיימים.
     * @param allSlots רשימת כל השעות האפשריות ביום נתון.
     * @return רשימה של השעות הפנויות.
     */
    public static List<String> calculateFreeSlots(DataSnapshot appointments, List<String> allSlots) {
        // יוצרים עותק של כל השעות
        List<String> freeSlots = new ArrayList<>(allSlots);

        // בודקים אילו שעות קיימות בתורים ומסירים אותן מהשעות הפנויות
        if (appointments != null) {
            for (DataSnapshot snapshot : appointments.getChildren()) {
                String slot = snapshot.getKey(); // שעת התור מה-Firebase
                if (slot != null) {
                    freeSlots.remove(slot); // הסרה מהרשימה של השעות הפנויות
                }
            }
        }
        return freeSlots;
    }

    /**
     * מייצר את כל השעות האפשריות ביום, מחולקות ל-30 דקות, בטווח שעות מוגדר מראש.
     *
     * @return רשימה של כל השעות הפנויות ביום נתון.
     */
    public static List<String> generateAllSlotsForDay() {
        List<String> slots = new ArrayList<>();
        for (int hour = 9; hour <= 21; hour++) { // טווח השעות: 9 בבוקר עד 21 בערב
            slots.add(hour + ":00"); // הוספת שעה עגולה
            slots.add(hour + ":30"); // הוספת חצי שעה
        }
        return slots;
    }
}
