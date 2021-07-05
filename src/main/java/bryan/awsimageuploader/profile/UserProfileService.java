package bryan.awsimageuploader.profile;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import bryan.awsimageuploader.bucket.BucketName;
import bryan.awsimageuploader.filestore.FileStore;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserProfileService {

  private final UserProfileDataAccessService userProfileDataAccessService;
  private final FileStore fileStore;

  @Autowired
  public UserProfileService(
      UserProfileDataAccessService userProfileDataAccessService,
      FileStore fileStore) {
    this.userProfileDataAccessService = userProfileDataAccessService;
    this.fileStore = fileStore;
  }

  List<UserProfile> getUserProfiles(){
    return userProfileDataAccessService.getUserProfiles();
  }

  public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
    //Check if file is empty
    isFileEmpty(file);
    //Check if file is an image
    isImage(file);
    //Check if User exist
    UserProfile user = getUserProfile(userProfileId);
    //Grab Metadata
    Map<String, String> metaData = getMetaData(file);
    //Store image in s3 and update database
    String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
    String filename = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());
    try {
      fileStore.save(path, filename, Optional.of(metaData), file.getInputStream());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private Map<String, String> getMetaData(MultipartFile file) {
    Map<String,String> metaData = new HashMap<>();
    metaData.put("Content-Type", file.getContentType());
    metaData.put("Content-Length",String.valueOf(file.getSize()));
    return metaData;
  }

  private UserProfile getUserProfile(UUID userProfileId) {
    return userProfileDataAccessService.getUserProfiles()
        .stream()
        .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            String.format("User profile: %s, not found", userProfileId)));
  }

  private void isImage(MultipartFile file) {
    if (!Arrays.asList(IMAGE_PNG.getMimeType(), IMAGE_JPEG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())){
        throw new IllegalStateException(("File uploaded must be an image [" + file.getContentType() + "]"));
    }
  }

  private void isFileEmpty(MultipartFile file) {
    if (file.isEmpty()){
      throw new IllegalStateException("file is empty");
    }
  }


}
