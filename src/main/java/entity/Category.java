package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private int cateId;

    @NotNull
    @Size(max = 255)
    @Column(name = "cate_name", nullable = false, length = 255)
    private String cateName;

    @Size(max = 255)
    @Column(name = "icons", length = 255)
    private String icons;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Owner

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Video> videos; 

    // Constructors
    public Category() {}

    // Getters and Setters
    public int getCateId() { return cateId; }
    public void setCateId(int cateId) { this.cateId = cateId; }
    public String getCateName() { return cateName; }
    public void setCateName(String cateName) { this.cateName = cateName; }
    public String getIcons() { return icons; }
    public void setIcons(String icons) { this.icons = icons; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Video> getVideos() { return videos; }
    public void setVideos(List<Video> videos) { this.videos = videos; }
}
