package controller;

import model.Kontak;
import model.KontakDAO;
import java.sql.SQLException;
import java.util.List;


public class KontakController {
    private KontakDAO contactDAO;

    public KontakController() {
        contactDAO = new KontakDAO();
    }

    // Mengambil semua data kontak
    public List<Kontak> getAllContacts() throws SQLException {
        return contactDAO.getAllContacts();
    }

    // Menambah data kontak
    public void addContact(String nama, String nomorTelepon, String kategori) throws SQLException {
        Kontak contact = new Kontak(0, nama, nomorTelepon, kategori);
        contactDAO.addContact(contact);
    }

    // Memperbarui kontak
    public void updateContact(int id, String nama, String nomorTelepon, String kategori) throws SQLException {
        Kontak contact = new Kontak(id, nama, nomorTelepon, kategori);
        contactDAO.updateContact(contact);
    }

    // Menghapus kontak
    public void deleteContact(int id) throws SQLException {
        contactDAO.deleteContact(id);
    }

    // Mencari kontak
    public List<Kontak> searchContacts(String keyword) throws SQLException {
        return contactDAO.searchContacts(keyword);
    }

    // Memeriksa nomor telepon duplikat
    public boolean isDuplicatePhoneNumber(String nomorTelepon, Integer excludeId) throws SQLException {
        return contactDAO.isDuplicatePhoneNumber(nomorTelepon, excludeId);
    }
}
