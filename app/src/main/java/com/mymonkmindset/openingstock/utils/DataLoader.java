package com.mymonkmindset.openingstock.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.mymonkmindset.openingstock.database.AppDatabase;
import com.mymonkmindset.openingstock.interfaces.Listable;
import com.mymonkmindset.openingstock.models.CallLogItem;
import com.mymonkmindset.openingstock.models.ContactItem;
import com.mymonkmindset.openingstock.models.Email;
import com.mymonkmindset.openingstock.models.Group;
import com.mymonkmindset.openingstock.models.Link;
import com.mymonkmindset.openingstock.models.Person;
import com.mymonkmindset.openingstock.models.PersonEmailMap;
import com.mymonkmindset.openingstock.models.PersonGroupMap;
import com.mymonkmindset.openingstock.models.PersonLinkMap;
import com.mymonkmindset.openingstock.models.PersonPhoneMap;
import com.mymonkmindset.openingstock.models.PersonReminderMap;
import com.mymonkmindset.openingstock.models.PersonWithPhone;
import com.mymonkmindset.openingstock.models.Phone;
import com.mymonkmindset.openingstock.models.Reminder;
import com.mymonkmindset.openingstock.models.ReminderWithPerson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataLoader {
    public static final ExecutorService diskIO = Executors.newSingleThreadExecutor();
    // Saving data

    public static void savePerson(Context context, Person person) {
        savePersonWithPhone(context, person, null, null);
    }

    public interface OnPersonsLoadedListener {
        void onLoaded(List<PersonWithPhone> list);
    }

    public interface OnRemindersLoadedListener {
        void onLoaded(List<ReminderWithPerson> list);
    }

    public static void savePersonWithPhone(Context context, Person person, String phoneNumber, Runnable onComplete) {
        savePersonFull(context, person, phoneNumber, null, null, null, onComplete);
    }

    public static void savePersonFull(Context context, Person person, String phoneNumber, String emailAddr, String linkUrl, String groupTitle, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            if (person.getStatus() == null) {
                person.setStatus(true);
            }
            long personId = db.personDao().insert(person);

            // 1. Phone
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                String cleanPhone = phoneNumber.trim();
                Phone existingPhone = db.phoneDao().getPhoneByNumber(cleanPhone);
                long phoneId;
                if (existingPhone != null && existingPhone.getId() != null) {
                    phoneId = existingPhone.getId();
                    existingPhone.setStatus(true);
                    db.phoneDao().update(existingPhone);
                } else {
                    Phone newPhone = Phone.builder()
                            .title(cleanPhone)
                            .verified(false)
                            .status(true)
                            .createdOn(String.valueOf(System.currentTimeMillis()))
                            .build();
                    phoneId = db.phoneDao().insert(newPhone);
                }
                PersonPhoneMap map = PersonPhoneMap.builder()
                        .personId(personId)
                        .phoneId(phoneId)
                        .mappedOn(String.valueOf(System.currentTimeMillis()))
                        .status(true)
                        .build();
                db.personPhoneMapDao().insert(map);
            }

            // 2. Email
            if (emailAddr != null && !emailAddr.trim().isEmpty()) {
                String cleanEmail = emailAddr.trim();
                Email existingEmail = db.emailDao().getEmailByAddress(cleanEmail);
                long emailId;
                if (existingEmail != null && existingEmail.getId() != null) {
                    emailId = existingEmail.getId();
                    existingEmail.setStatus(true);
                    db.emailDao().update(existingEmail);
                } else {
                    Email newEmail = Email.builder()
                            .title(cleanEmail)
                            .verified(false)
                            .status(true)
                            .createdOn(String.valueOf(System.currentTimeMillis()))
                            .build();
                    emailId = db.emailDao().insert(newEmail);
                }
                PersonEmailMap map = PersonEmailMap.builder()
                        .personId(personId)
                        .emailId(emailId)
                        .mappedOn(String.valueOf(System.currentTimeMillis()))
                        .status(true)
                        .build();
                db.personEmailMapDao().insert(map);
            }

            // 3. Link
            if (linkUrl != null && !linkUrl.trim().isEmpty()) {
                String cleanLink = linkUrl.trim();
                Link existingLink = db.linkDao().getLinkByUrl(cleanLink);
                long linkId;
                if (existingLink != null && existingLink.getId() != null) {
                    linkId = existingLink.getId();
                    existingLink.setStatus(true);
                    db.linkDao().update(existingLink);
                } else {
                    Link newLink = Link.builder()
                            .title(cleanLink)
                            .url(cleanLink)
                            .status(true)
                            .createdOn(String.valueOf(System.currentTimeMillis()))
                            .build();
                    linkId = db.linkDao().insert(newLink);
                }
                PersonLinkMap map = PersonLinkMap.builder()
                        .personId(personId)
                        .linkId(linkId)
                        .mappedOn(String.valueOf(System.currentTimeMillis()))
                        .status(true)
                        .build();
                db.personLinkMapDao().insert(map);
            }

            // 4. Group
            if (groupTitle != null && !groupTitle.trim().isEmpty()) {
                String cleanGroup = groupTitle.trim();
                Group existingGroup = db.groupDao().getGroupByTitle(cleanGroup);
                long groupId;
                if (existingGroup != null && existingGroup.getId() != null) {
                    groupId = existingGroup.getId();
                    existingGroup.setStatus(true);
                    db.groupDao().update(existingGroup);
                } else {
                    Group newGroup = Group.builder()
                            .title(cleanGroup)
                            .status(true)
                            .createdOn(String.valueOf(System.currentTimeMillis()))
                            .build();
                    groupId = db.groupDao().insert(newGroup);
                }
                PersonGroupMap map = PersonGroupMap.builder()
                        .personId(personId)
                        .groupId(groupId)
                        .mappedOn(String.valueOf(System.currentTimeMillis()))
                        .status(true)
                        .build();
                db.personGroupMapDao().insert(map);
            }

            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void updatePersonWithPhone(Context context, Person person, String phoneNumber, Runnable onComplete) {
        updatePersonFull(context, person, phoneNumber, null, null, null, onComplete);
    }

    public static void updatePersonFull(Context context, Person person, String phoneNumber, String emailAddr, String linkUrl, String groupTitle, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.personDao().update(person);

            if (person.getPeople_id() != null) {
                long personId = person.getPeople_id();

                // 1. Phone
                if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                    String cleanPhone = phoneNumber.trim();
                    Phone existingPhone = db.personPhoneMapDao().getPhoneForPerson(personId);
                    if (existingPhone != null) {
                        existingPhone.setTitle(cleanPhone);
                        existingPhone.setStatus(true);
                        db.phoneDao().update(existingPhone);
                    } else {
                        Phone newPhone = Phone.builder()
                                .title(cleanPhone)
                                .verified(false)
                                .status(true)
                                .createdOn(String.valueOf(System.currentTimeMillis()))
                                .build();
                        long phoneId = db.phoneDao().insert(newPhone);
                        PersonPhoneMap map = PersonPhoneMap.builder()
                                .personId(personId)
                                .phoneId(phoneId)
                                .mappedOn(String.valueOf(System.currentTimeMillis()))
                                .status(true)
                                .build();
                        db.personPhoneMapDao().insert(map);
                    }
                }

                // 2. Email
                if (emailAddr != null && !emailAddr.trim().isEmpty()) {
                    String cleanEmail = emailAddr.trim();
                    Email existingEmail = db.personEmailMapDao().getEmailForPerson(personId);
                    if (existingEmail != null) {
                        existingEmail.setTitle(cleanEmail);
                        existingEmail.setStatus(true);
                        db.emailDao().update(existingEmail);
                    } else {
                        Email newEmail = Email.builder()
                                .title(cleanEmail)
                                .verified(false)
                                .status(true)
                                .createdOn(String.valueOf(System.currentTimeMillis()))
                                .build();
                        long emailId = db.emailDao().insert(newEmail);
                        PersonEmailMap map = PersonEmailMap.builder()
                                .personId(personId)
                                .emailId(emailId)
                                .mappedOn(String.valueOf(System.currentTimeMillis()))
                                .status(true)
                                .build();
                        db.personEmailMapDao().insert(map);
                    }
                }

                // 3. Link
                if (linkUrl != null && !linkUrl.trim().isEmpty()) {
                    String cleanLink = linkUrl.trim();
                    Link existingLink = db.personLinkMapDao().getLinkForPerson(personId);
                    if (existingLink != null) {
                        existingLink.setTitle(cleanLink);
                        existingLink.setUrl(cleanLink);
                        existingLink.setStatus(true);
                        db.linkDao().update(existingLink);
                    } else {
                        Link newLink = Link.builder()
                                .title(cleanLink)
                                .url(cleanLink)
                                .status(true)
                                .createdOn(String.valueOf(System.currentTimeMillis()))
                                .build();
                        long linkId = db.linkDao().insert(newLink);
                        PersonLinkMap map = PersonLinkMap.builder()
                                .personId(personId)
                                .linkId(linkId)
                                .mappedOn(String.valueOf(System.currentTimeMillis()))
                                .status(true)
                                .build();
                        db.personLinkMapDao().insert(map);
                    }
                }

                // 4. Group
                if (groupTitle != null && !groupTitle.trim().isEmpty()) {
                    String cleanGroup = groupTitle.trim();
                    Group existingGroup = db.personGroupMapDao().getGroupForPerson(personId);
                    if (existingGroup != null) {
                        existingGroup.setTitle(cleanGroup);
                        existingGroup.setStatus(true);
                        db.groupDao().update(existingGroup);
                    } else {
                        Group newGroup = Group.builder()
                                .title(cleanGroup)
                                .status(true)
                                .createdOn(String.valueOf(System.currentTimeMillis()))
                                .build();
                        long groupId = db.groupDao().insert(newGroup);
                        PersonGroupMap map = PersonGroupMap.builder()
                                .personId(personId)
                                .groupId(groupId)
                                .mappedOn(String.valueOf(System.currentTimeMillis()))
                                .status(true)
                                .build();
                        db.personGroupMapDao().insert(map);
                    }
                }
            }

            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void softDeletePerson(Context context, long personId, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.personDao().updatePersonStatus(personId, false);
            db.personPhoneMapDao().updateMapStatusForPerson(personId, false);
            db.personEmailMapDao().updateMapStatusForPerson(personId, false);
            db.personLinkMapDao().updateMapStatusForPerson(personId, false);
            db.personGroupMapDao().updateMapStatusForPerson(personId, false);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void restorePerson(Context context, long personId, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.personDao().updatePersonStatus(personId, true);
            db.personPhoneMapDao().updateMapStatusForPerson(personId, true);
            db.personEmailMapDao().updateMapStatusForPerson(personId, true);
            db.personLinkMapDao().updateMapStatusForPerson(personId, true);
            db.personGroupMapDao().updateMapStatusForPerson(personId, true);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void saveReminder(Context context, Reminder reminder, String personName, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            if (reminder.getStatus() == null) {
                reminder.setStatus(true);
            }
            if (reminder.getIsCompleted() == null) {
                reminder.setIsCompleted(false);
            }
            long reminderId = db.reminderDao().insert(reminder);
            reminder.setId(reminderId);

            if (personName != null && !personName.trim().isEmpty()) {
                String cleanName = personName.trim();
                List<Person> activePersons = db.personDao().getActivePersons();
                Person matched = null;
                for (Person p : activePersons) {
                    if (cleanName.equalsIgnoreCase(p.getName()) || cleanName.equalsIgnoreCase(p.getTitle())) {
                        matched = p;
                        break;
                    }
                }
                if (matched != null && matched.getPeople_id() != null) {
                    PersonReminderMap map = PersonReminderMap.builder()
                            .personId(matched.getPeople_id())
                            .reminderId(reminderId)
                            .mappedOn(String.valueOf(System.currentTimeMillis()))
                            .status(true)
                            .build();
                    db.personReminderMapDao().insert(map);
                }
            }

            ReminderNotificationHelper.scheduleNotification(context, reminder);

            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void updateReminder(Context context, Reminder reminder, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.reminderDao().update(reminder);
            ReminderNotificationHelper.scheduleNotification(context, reminder);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void softDeleteReminder(Context context, long reminderId, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.reminderDao().updateReminderStatus(reminderId, false);
            ReminderNotificationHelper.cancelNotification(context, reminderId);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void restoreReminder(Context context, long reminderId, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.reminderDao().updateReminderStatus(reminderId, true);
            Reminder reminder = db.reminderDao().getReminderById(reminderId);
            if (reminder != null) {
                ReminderNotificationHelper.scheduleNotification(context, reminder);
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void toggleReminderCompletion(Context context, long reminderId, boolean completed, Runnable onComplete) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.reminderDao().updateReminderCompletion(reminderId, completed);
            if (completed) {
                ReminderNotificationHelper.cancelNotification(context, reminderId);
            } else {
                Reminder reminder = db.reminderDao().getReminderById(reminderId);
                if (reminder != null) {
                    ReminderNotificationHelper.scheduleNotification(context, reminder);
                }
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static void loadReminders(Context context, boolean includeDeleted, OnRemindersLoadedListener listener) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            List<Reminder> reminders = includeDeleted
                    ? db.reminderDao().getAllRemindersIncludingDeleted()
                    : db.reminderDao().getActiveReminders();

            List<ReminderWithPerson> result = new ArrayList<>();
            for (Reminder r : reminders) {
                result.add(new ReminderWithPerson(r, ""));
            }

            if (listener != null) {
                listener.onLoaded(result);
            }
        });
    }

    public static void loadPersonsWithPhones(Context context, boolean includeDeleted, OnPersonsLoadedListener listener) {
        DataLoader.diskIO.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            List<Person> persons = includeDeleted
                    ? db.personDao().getAllPersonsIncludingDeleted()
                    : db.personDao().getActivePersons();

            List<PersonWithPhone> result = new ArrayList<>();
            for (Person p : persons) {
                if (p.getPeople_id() != null) {
                    Phone phone = db.personPhoneMapDao().getPhoneForPerson(p.getPeople_id());
                    String phoneNum = phone != null ? phone.getTitle() : "";

                    Email email = db.personEmailMapDao().getEmailForPerson(p.getPeople_id());
                    String emailStr = email != null ? email.getTitle() : "";

                    Link link = db.personLinkMapDao().getLinkForPerson(p.getPeople_id());
                    String linkStr = link != null ? link.getUrl() : "";

                    Group group = db.personGroupMapDao().getGroupForPerson(p.getPeople_id());
                    String groupStr = group != null ? group.getTitle() : "";

                    result.add(new PersonWithPhone(p, phoneNum, emailStr, linkStr, groupStr));
                }
            }

            if (listener != null) {
                listener.onLoaded(result);
            }
        });
    }


    public static void loadPersonData(List<Listable> data) {
        data.clear();
        List<Listable> list = Arrays.asList(
                Person.builder().title("Tapas").build(),
                Person.builder().title("Vaibhaw").build(),
                Person.builder().title("Laxmi").build(),
                Person.builder().title("Ashesh").build(),
                Person.builder().title("Aarush").build(),
                Person.builder().title("Abhay").build(),
                Person.builder().title("Ayushi").build()
        );
        data.addAll(list);
    }

    public static void loadContactData(List<ContactItem> contacts) {
        contacts.clear();
        contacts.add(new ContactItem("Ankur Goyal", "+91 98765 43210", "Google"));
        contacts.add(new ContactItem("Monk Mindset", "+91 98765 12345", "SIM 1"));
        contacts.add(new ContactItem("Rahul Sharma", "+91 98123 45678", "SIM 2"));
        contacts.add(new ContactItem("Priya Verma", "+91 99887 76655", "Device"));
        contacts.add(new ContactItem("Amit Kumar", "+91 97654 32109", "Google"));
    }

    public static void loadRealContacts(Context context, List<ContactItem> contacts) {
        contacts.clear();
        if (context == null) return;

        Set<String> seenNumbers = new HashSet<>();
        ContentResolver contentResolver = context.getContentResolver();

        // 1. Query ContactsContract for Phone & SIM contacts
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.RawContacts.ACCOUNT_TYPE
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

        try (Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder)) {
            if (cursor != null) {
                int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int numberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int accountIdx = cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE);

                while (cursor.moveToNext()) {
                    String name = nameIdx != -1 ? cursor.getString(nameIdx) : "Unknown";
                    String number = numberIdx != -1 ? cursor.getString(numberIdx) : "";
                    String accountType = accountIdx != -1 ? cursor.getString(accountIdx) : "";

                    if (number == null || number.trim().isEmpty()) continue;

                    String cleanNumber = number.replaceAll("[^0-9+]", "");
                    if (cleanNumber.isEmpty() || seenNumbers.contains(cleanNumber)) continue;
                    seenNumbers.add(cleanNumber);

                    String badge = "Device";
                    if (accountType != null) {
                        String lower = accountType.toLowerCase();
                        if (lower.contains("sim") || lower.contains("icc") || lower.contains("adn")) {
                            badge = "SIM";
                        } else if (lower.contains("google") || lower.contains("gmail")) {
                            badge = "Google";
                        } else if (lower.contains("snapchat")) {
                            badge = "Snapchat";
                        } else if (lower.contains("whatsapp")) {
                            badge = "WhatsApp";
                        }
                    }

                    contacts.add(new ContactItem(name, number, badge));
                }
            }
        } catch (Exception ignored) {
        }

        // Fallback sample contacts if device cursor returned no contacts
        if (contacts.isEmpty()) {
            loadContactData(contacts);
        }
    }

    public static void loadRealCallLogs(Context context, List<CallLogItem> callLogs) {
        callLogs.clear();
        if (context == null) return;

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };
        String sortOrder = CallLog.Calls.DATE + " DESC";

        try (Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder)) {
            if (cursor != null) {
                int nameIdx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int numberIdx = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int typeIdx = cursor.getColumnIndex(CallLog.Calls.TYPE);
                int dateIdx = cursor.getColumnIndex(CallLog.Calls.DATE);
                int durationIdx = cursor.getColumnIndex(CallLog.Calls.DURATION);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());

                while (cursor.moveToNext()) {
                    String number = numberIdx != -1 ? cursor.getString(numberIdx) : "";
                    String name = nameIdx != -1 ? cursor.getString(nameIdx) : "";

                    if (name == null || name.trim().isEmpty()) {
                        name = lookupContactName(context, number);
                    }
                    if (name == null || name.trim().isEmpty()) {
                        name = number;
                    }

                    int typeCode = typeIdx != -1 ? cursor.getInt(typeIdx) : 1;
                    String callType = "Incoming";
                    if (typeCode == CallLog.Calls.OUTGOING_TYPE) {
                        callType = "Outgoing";
                    } else if (typeCode == CallLog.Calls.MISSED_TYPE) {
                        callType = "Missed";
                    } else if (typeCode == CallLog.Calls.REJECTED_TYPE) {
                        callType = "Rejected";
                    }

                    if ((name != null && name.toLowerCase().contains("snapchat")) ||
                        (number != null && number.toLowerCase().contains("snapchat"))) {
                        callType = "Snapchat " + callType;
                    }

                    long dateMillis = dateIdx != -1 ? cursor.getLong(dateIdx) : 0;
                    String formattedDate = dateMillis > 0 ? dateFormat.format(new Date(dateMillis)) : "Recent";

                    long durationSec = durationIdx != -1 ? cursor.getLong(durationIdx) : 0;
                    String durationStr = durationSec > 0 ? (durationSec / 60) + "m " + (durationSec % 60) + "s" : "0s";

                    callLogs.add(new CallLogItem(name, number, callType, String.valueOf(dateMillis), String.valueOf(durationSec)));
                }
            }
        } catch (Exception ignored) {
        }

        if (callLogs.isEmpty()) {
            loadCallLogData(callLogs);
        }
    }

    private static String lookupContactName(Context context, String phoneNumber) {
        if (context == null || phoneNumber == null || phoneNumber.trim().isEmpty()) return "";
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIdx = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    if (nameIdx != -1) {
                        return cursor.getString(nameIdx);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public static void loadCallLogData(List<CallLogItem> callLogs) {
        callLogs.clear();
        callLogs.add(new CallLogItem("Ankur Goyal", "+91 98765 43210", "Incoming", "Today, 10:30 AM", "3m 45s"));
        callLogs.add(new CallLogItem("Snapchat - Siddharth", "@siddharth_snap", "Snapchat Audio", "Today, 10:05 AM", "4m 12s"));
        callLogs.add(new CallLogItem("Monk Mindset", "+91 98765 12345", "Outgoing", "Today, 09:15 AM", "1m 20s"));
        callLogs.add(new CallLogItem("Snapchat - Riya Kapoor", "@riyakapoor_snap", "Snapchat Video", "Today, 08:30 AM", "8m 45s"));
        callLogs.add(new CallLogItem("Rahul Sharma", "+91 98123 45678", "Missed", "Yesterday, 06:45 PM", "0s"));
        callLogs.add(new CallLogItem("Snapchat - Aman Gupta", "@amangupta_snap", "Snapchat Missed", "Yesterday, 04:20 PM", "0s"));
        callLogs.add(new CallLogItem("Priya Verma", "+91 99887 76655", "Incoming", "Yesterday, 02:10 PM", "5m 10s"));
        callLogs.add(new CallLogItem("Amit Kumar", "+91 97654 32109", "Missed", "2 days ago", "0s"));
    }
}
