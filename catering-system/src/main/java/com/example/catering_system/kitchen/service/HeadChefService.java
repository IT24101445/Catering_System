package com.example.catering_system.kitchen.service;

import com.example.catering_system.kitchen.dao.EventChefDAO;
import com.example.catering_system.kitchen.dao.MenuDAO;
import com.example.catering_system.kitchen.dao.NotificationChefDAO;
import com.example.catering_system.kitchen.dao.ScheduleDAO;
import com.example.catering_system.kitchen.model.EventChef;
import com.example.catering_system.kitchen.model.Menu;
import com.example.catering_system.kitchen.model.NotificationChef;
import com.example.catering_system.kitchen.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadChefService {

    private final DataSource dataSource;

    @Autowired
    public HeadChefService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ------------- Confirmed Menus Access -------------

    public List<Menu> getConfirmedMenus() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new MenuDAO(conn).getConfirmedMenus();
        }
    }

    public Menu getConfirmedMenuById(int id) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Menu menu = new MenuDAO(conn).getMenuById(id);
            if (menu != null && menu.isConfirmed()) {
                return menu;
            }
            return null;
        }
    }

    // ------------- Guest Count and Event Information -------------

    public List<EventChef> getConfirmedEvents() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new EventChefDAO(conn).getConfirmedEvents();
        }
    }

    public EventChef getEventById(int id) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new EventChefDAO(conn).getEventById(id);
        }
    }

    public List<EventChef> getUpcomingEvents() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new EventChefDAO(conn).getUpcomingEvents();
        }
    }

    public int getGuestCountForEvent(int eventId) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            EventChef event = new EventChefDAO(conn).getEventById(eventId);
            return event != null ? event.getGuestCount() : 0;
        }
    }

    public int getGuestCountForMenu(int menuId) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Menu menu = new MenuDAO(conn).getMenuById(menuId);
            if (menu != null && menu.getEventId() != null) {
                return getGuestCountForEvent(menu.getEventId());
            }
            return 0;
        }
    }

    // ------------- Delivery Times and Cooking Schedules -------------

    public List<EventChef> getEventsWithDeliveryTimes() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new EventChefDAO(conn).getUpcomingEvents();
        }
    }

    public List<Schedule> getCookingSchedules() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new ScheduleDAO(conn).getSchedules();
        }
    }

    public List<Schedule> getSchedulesByEvent(int eventId) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            // This would need to be implemented in ScheduleDAO
            // For now, return all schedules
            return new ScheduleDAO(conn).getSchedules();
        }
    }

    // ------------- Notifications -------------

    public List<NotificationChef> getUnreadNotifications() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new NotificationChefDAO(conn).getUnreadNotifications();
        }
    }

    public List<NotificationChef> getMenuChangeNotifications() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new NotificationChefDAO(conn).getMenuChangeNotifications();
        }
    }

    public List<NotificationChef> getAllNotifications() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new NotificationChefDAO(conn).getAllNotifications();
        }
    }

    public void markNotificationAsRead(int notificationId) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            new NotificationChefDAO(conn).markAsRead(notificationId);
        }
    }

    public void markAllNotificationsAsRead() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            new NotificationChefDAO(conn).markAllAsRead();
        }
    }

    public int getUnreadNotificationCount() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return new NotificationChefDAO(conn).countUnread();
        }
    }

    // ------------- Create Notifications -------------

    public void createMenuChangeNotification(int menuId, String menuName, String changeDescription) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            String title = "Menu Change: " + menuName;
            String message = "Menu '" + menuName + "' has been updated: " + changeDescription;
            new NotificationChefDAO(conn).insertNotification(title, message, "menu_change", menuId, "menu");
        }
    }

    public void createEventUpdateNotification(int eventId, String eventName, String updateDescription) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            String title = "Event Update: " + eventName;
            String message = "Event '" + eventName + "' has been updated: " + updateDescription;
            new NotificationChefDAO(conn).insertNotification(title, message, "event_update", eventId, "event");
        }
    }

    public void createScheduleUpdateNotification(int scheduleId, String updateDescription) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            String title = "Schedule Update";
            String message = "Cooking schedule has been updated: " + updateDescription;
            new NotificationChefDAO(conn).insertNotification(title, message, "schedule_update", scheduleId, "schedule");
        }
    }

    // ------------- Dashboard Data -------------

    public HeadChefDashboardData getDashboardData() throws Exception {
        HeadChefDashboardData data = new HeadChefDashboardData();
        
        try (Connection conn = dataSource.getConnection()) {
            // Initialize with empty lists to avoid null pointer exceptions
            data.setConfirmedMenus(new ArrayList<>());
            data.setUpcomingEvents(new ArrayList<>());
            data.setUnreadNotifications(new ArrayList<>());
            data.setCookingSchedules(new ArrayList<>());
            data.setUnreadNotificationCount(0);
            
            try {
                // Get confirmed menus
                data.setConfirmedMenus(new MenuDAO(conn).getConfirmedMenus());
            } catch (Exception e) {
                System.err.println("Error getting confirmed menus: " + e.getMessage());
                e.printStackTrace();
            }
            
            try {
                // Get upcoming events
                data.setUpcomingEvents(new EventChefDAO(conn).getUpcomingEvents());
            } catch (Exception e) {
                System.err.println("Error getting upcoming events: " + e.getMessage());
                e.printStackTrace();
            }
            
            try {
                // Get unread notifications
                data.setUnreadNotifications(new NotificationChefDAO(conn).getUnreadNotifications());
            } catch (Exception e) {
                System.err.println("Error getting unread notifications: " + e.getMessage());
                e.printStackTrace();
            }
            
            try {
                // Get unread count
                data.setUnreadNotificationCount(new NotificationChefDAO(conn).countUnread());
            } catch (Exception e) {
                System.err.println("Error getting unread count: " + e.getMessage());
                e.printStackTrace();
            }
            
            try {
                // Get cooking schedules
                data.setCookingSchedules(new ScheduleDAO(conn).getSchedules());
            } catch (Exception e) {
                System.err.println("Error getting cooking schedules: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return data;
    }

    // ------------- Sample Data Creation -------------
    
    public void createSampleData() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            // Create sample confirmed menu
            MenuDAO menuDAO = new MenuDAO(conn);
            int menuId = menuDAO.insertMenu("Wedding Reception Menu", "confirmed", 1);
            
            // Create sample event
            EventChefDAO eventDAO = new EventChefDAO(conn);
            eventDAO.insertEvent("Wedding Reception", 150, 
                java.time.LocalDateTime.now().plusDays(7), 
                "Grand Hotel Ballroom", "confirmed", 
                "Elegant wedding reception for 150 guests");
            
            // Create sample notification
            NotificationChefDAO notificationDAO = new NotificationChefDAO(conn);
            notificationDAO.insertNotification(
                "New Confirmed Menu", 
                "Wedding Reception Menu has been confirmed and is ready for preparation.", 
                "menu_change", menuId, "menu");
        }
    }

    // ------------- Helper Classes -------------

    public static class HeadChefDashboardData {
        private List<Menu> confirmedMenus;
        private List<EventChef> upcomingEvents;
        private List<NotificationChef> unreadNotifications;
        private List<Schedule> cookingSchedules;
        private int unreadNotificationCount;

        // Getters and Setters
        public List<Menu> getConfirmedMenus() { return confirmedMenus; }
        public void setConfirmedMenus(List<Menu> confirmedMenus) { this.confirmedMenus = confirmedMenus; }

        public List<EventChef> getUpcomingEvents() { return upcomingEvents; }
        public void setUpcomingEvents(List<EventChef> upcomingEvents) { this.upcomingEvents = upcomingEvents; }

        public List<NotificationChef> getUnreadNotifications() { return unreadNotifications; }
        public void setUnreadNotifications(List<NotificationChef> unreadNotifications) { this.unreadNotifications = unreadNotifications; }

        public List<Schedule> getCookingSchedules() { return cookingSchedules; }
        public void setCookingSchedules(List<Schedule> cookingSchedules) { this.cookingSchedules = cookingSchedules; }

        public int getUnreadNotificationCount() { return unreadNotificationCount; }
        public void setUnreadNotificationCount(int unreadNotificationCount) { this.unreadNotificationCount = unreadNotificationCount; }
    }
}
