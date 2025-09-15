package dao;

import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UserDAO {

    public User login(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", 
                User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Không tìm thấy người dùng
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đăng nhập: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void updateProfile(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User existing = em.find(User.class, user.getId());
            if (existing == null) {
                throw new RuntimeException("Người dùng với id=" + user.getId() + " không tồn tại");
            }
            // Chỉ cập nhật các trường được phép
            existing.setFullname(user.getFullname());
            existing.setPassword(user.getPassword());
            existing.setPhone(user.getPhone());
            existing.setImage(user.getImage());
            em.merge(existing);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Lỗi khi cập nhật hồ sơ: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public User findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            User user = em.find(User.class, id);
            if (user == null) {
                throw new RuntimeException("Người dùng với id=" + id + " không tồn tại");
            }
            return user;
        } finally {
            em.close();
        }
    }
}