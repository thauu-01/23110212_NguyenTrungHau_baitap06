package dao;

import entity.Category;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CategoryDAO {

    public void create(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Lỗi khi tạo danh mục: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void update(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Category existing = em.find(Category.class, category.getCateId());
            if (existing == null) {
                throw new RuntimeException("Danh mục với cate_id=" + category.getCateId() + " không tồn tại");
            }
            em.merge(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Lỗi khi cập nhật danh mục: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void delete(int cateId, User currentUser) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Category cat = em.find(Category.class, cateId);
            if (cat == null) {
                throw new RuntimeException("Danh mục với cate_id=" + cateId + " không tồn tại");
            }
            if (cat.getUser().getId() != currentUser.getId()) {
                throw new RuntimeException("Bạn không có quyền xóa danh mục này");
            }
            em.getTransaction().begin();
            em.remove(cat);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Lỗi khi xóa danh mục: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Category findById(int cateId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c JOIN FETCH c.user WHERE c.cateId = :cateId", Category.class);
            query.setParameter("cateId", cateId);
            Category cat = query.getSingleResult();
            if (cat == null) {
                throw new RuntimeException("Danh mục với cate_id=" + cateId + " không tồn tại");
            }
            return cat;
        } catch (NoResultException e) {
            throw new RuntimeException("Danh mục với cate_id=" + cateId + " không tồn tại", e);
        } finally {
            em.close();
        }
    }

    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c JOIN FETCH c.user", Category.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Category> findByUserId(int userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c JOIN FETCH c.user WHERE c.user.id = :userId", Category.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Category> searchByCateName(String keyword, int userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Category c JOIN FETCH c.user WHERE c.cateName LIKE :keyword AND c.user.id = :userId";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("keyword", "%" + (keyword == null ? "" : keyword) + "%");
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}