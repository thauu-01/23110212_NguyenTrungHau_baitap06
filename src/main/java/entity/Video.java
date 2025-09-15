package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private int videoId;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @NotNull
    @Size(max = 255)
    @Column(name = "video_file", nullable = false, length = 255)
    private String videoFile;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_id", nullable = false)
    private Category category;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    // Constructors
    public Video() {}

    // Getters and Setters
    public int getVideoId() { return videoId; }
    public void setVideoId(int videoId) { this.videoId = videoId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVideoFile() { return videoFile; }
    public void setVideoFile(String videoFile) { this.videoFile = videoFile; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
}