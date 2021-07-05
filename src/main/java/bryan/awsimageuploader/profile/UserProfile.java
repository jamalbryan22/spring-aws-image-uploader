package bryan.awsimageuploader.profile;


import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserProfile {

  private UUID userProfileId;
  private String username;
  private String profileImageKey; //S3 key

  public UserProfile(UUID userProfileId, String username, String profileImageKey) {
    this.userProfileId = userProfileId;
    this.username = username;
    this.profileImageKey = profileImageKey;
  }

  public UUID getUserProfileId() {
    return userProfileId;
  }

  public void setUserProfileId(UUID userProfileId) {
    this.userProfileId = userProfileId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Optional<String> getProfileImageKey() {
    return Optional.ofNullable(profileImageKey);
  }

  public void setProfileImageKey(String profileImageKey) {
    this.profileImageKey = profileImageKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserProfile that = (UserProfile) o;
    return Objects.equals(userProfileId, that.userProfileId) &&
           Objects.equals(username, that.username) &&
           Objects.equals(profileImageKey, that.profileImageKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userProfileId, username, profileImageKey);
  }
}
