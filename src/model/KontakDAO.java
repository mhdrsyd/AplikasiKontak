package model;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KontakDAO {

    public List<Kontak> getAllContacts() throws SQLException {
        List<Kontak> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                contacts.add(new Kontak(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("nomor_telepon"),
                        rs.getString("kategori")
                ));
            }
        }
        return contacts;
    }

    public void addContact(Kontak contact) throws SQLException {
        String sql = "INSERT INTO contacts (nama, nomor_telepon, kategori) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getNama());
            pstmt.setString(2, contact.getNomorTelepon());
            pstmt.setString(3, contact.getKategori());
            pstmt.executeUpdate();
        }
    }

    public void updateContact(Kontak contact) throws SQLException {
        String sql = "UPDATE contacts SET nama=?, nomor_telepon=?, kategori=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getNama());
            pstmt.setString(2, contact.getNomorTelepon());
            pstmt.setString(3, contact.getKategori());
            pstmt.setInt(4, contact.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteContact(int id) throws SQLException {
        String sql = "DELETE FROM contacts WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Kontak> searchContacts(String keyword) throws SQLException {
        List<Kontak> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE nama LIKE ? OR nomor_telepon LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                contacts.add(new Kontak(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("nomor_telepon"),
                        rs.getString("kategori")
                ));
            }
        }
        return contacts;
    }

    public boolean isDuplicatePhoneNumber(String nomorTelepon, Integer excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM contacts WHERE nomor_telepon = ?" +
                (excludeId != null ? " AND id != ?" : "");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomorTelepon);
            if (excludeId != null) pstmt.setInt(2, excludeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }
}
