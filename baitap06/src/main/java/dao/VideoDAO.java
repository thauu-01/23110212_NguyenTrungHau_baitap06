package dao;

import entity.User;
import entity.Video;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VideoDAO {

    public void create(Video video) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(video);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Lỗi khi tạo video: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void update(Video video) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Video existing = em.find(Video.class, video.getVideoId());
            if (existing == null) {
                throw new RuntimeException("Video với video_id=" + video.getVideoId() + " không tồn tại");
            }
            em.merge(video);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Lỗi khi cập nhật video: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void delete(int videoId, User currentUser) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Video video = em.find(Video.class, videoId);
            if (video == null) {
                throw new RuntimeException("Video với video_id=" + videoId + " không tồn tại");
            }
            // Cho phép Admin (roleid = 3) xóa bất kỳ video nào
            if (currentUser.getRoleid() != 3 && video.getUser().getId() != currentUser.getId()) {
                throw new RuntimeException("Bạn không có quyền xóa video này");
            }
            em.getTransaction().begin();
            em.remove(video);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Lỗi khi xóa video: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Video findById(int videoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Video> query = em.createQuery(
                "SELECT v FROM Video v JOIN FETCH v.user JOIN FETCH v.category WHERE v.videoId = :videoId", Video.class);
            query.setParameter("videoId", videoId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Video> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Video> query = em.createQuery(
                "SELECT v FROM Video v JOIN FETCH v.user JOIN FETCH v.category", Video.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Video> findByUserId(int userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Video> query = em.createQuery(
                "SELECT v FROM Video v JOIN FETCH v.user JOIN FETCH v.category WHERE v.user.id = :userId", Video.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Video> searchByTitleOrDescription(String keyword, int userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user JOIN FETCH v.category " +
                         "WHERE (v.title LIKE :keyword OR v.description LIKE :keyword) AND v.user.id = :userId";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setParameter("keyword", "%" + (keyword == null ? "" : keyword) + "%");
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}