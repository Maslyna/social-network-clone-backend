package net.maslyna.user.integration.test;

import lombok.extern.slf4j.Slf4j;
import net.maslyna.user.integration.ServiceURI;
import net.maslyna.user.model.dto.request.EditUserRequest;
import net.maslyna.user.model.dto.request.UserRegistrationRequest;
import net.maslyna.user.model.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.UUID;

import static net.maslyna.user.integration.config.MockFileServiceConfig.configureDelete;
import static net.maslyna.user.integration.config.MockFileServiceConfig.configureSave;
import static net.maslyna.user.integration.config.MockSecurityServiceConfig.configureSecurityRegistration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class UserServiceTests extends BasicIntegrationTest {

    private static final UserRegistrationRequest defaultRegistrationRequest =
            UserRegistrationRequest.builder()
                    .email("dummymail@mail.net")
                    .password("not-real-password")
                    .build();

    @Test
    public void userRegistrationTests() throws Exception {
        configureSecurityRegistration(mockSecurityServer);
        userRegistration_ReturnsCreated();
        userRegistrationWithOccupiedEmail_ReturnsConflict();
        userRegistrationEmptyBody_ReturnsBadRequest();
    }

    @Test
    public void editUserTests() throws Exception {
        configureSecurityRegistration(mockSecurityServer);
        final long userId = userRegistration(defaultRegistrationRequest);
        validUserEditTest_ReturnsOk(userId);
        notValidEditTest_ReturnsBadRequest(userId);
    }

    @Test
    public void getUsersTests() throws Exception {
        configureSecurityRegistration(mockSecurityServer);
        userRegistration(defaultRegistrationRequest);
        emptyUserHeaderGetAllUsers_ReturnsOk();
        getAllUsers_ReturnsOk();
        notValidParamsGetAllUsers_ReturnsOk();
    }

    @Test
    public void setUserPhotoTests() throws Exception {
        configureSecurityRegistration(mockSecurityServer);
        configureSave(mockFileServer);
        long userId = userRegistration(defaultRegistrationRequest);
        setUserPhoto_ReturnsOk(userId);
    }

    @Test
    public void addUserPhotoTests() throws Exception {
        configureSecurityRegistration(mockSecurityServer);
        configureSave(mockFileServer);
        long userId = userRegistration(defaultRegistrationRequest);
        addUserPhoto_ReturnsOk(userId);
    }

    @Test
    public void removeUserPhotoTests() throws Exception {
        configureSecurityRegistration(mockSecurityServer);
        final long userId = userRegistration(defaultRegistrationRequest);
        final long notValidUser = userRegistration(new UserRegistrationRequest("notvalid@mail.com", "password123"));
        configureSave(mockFileServer);
        final UUID photoId = addUserPhoto_ReturnsOk(userId);
        configureDelete(mockFileServer, photoId);

        notValidUserRemovePhoto_ReturnsForbidden(notValidUser, photoId);
        removeUserPhoto_ReturnsOk(userId, photoId);
        removeUserPhotoSecondTime_ReturnsNotFound(userId, photoId);
    }

    @Test
    public void getUserPhotoTests() throws Exception {
        configureSecurityRegistration(mockSecurityServer);
        final long userId = userRegistration(defaultRegistrationRequest);
        final long anotherUserId =  userRegistration(new UserRegistrationRequest("notvalid@mail.com", "password123"));
        configureSave(mockFileServer);
        final UUID photoId = addUserPhoto_ReturnsOk(userId);

        EditUserRequest editUserRequest = EditUserRequest.builder().isPublicAccount(false).build();
        editUser(userId, editUserRequest);

        getUserPhotos_ReturnsOk(userId);
        getNotPublicPhotos_ReturnsForbidden(userId, anotherUserId);
    }

    private void getNotPublicPhotos_ReturnsForbidden(long userId, long anotherUserId) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_USER_PHOTOS.formatted(userId))
                        .header(USER_HEADER, anotherUserId)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    private void getUserPhotos_ReturnsOk(long userId) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_USER_PHOTOS.formatted(userId))
                        .header(USER_HEADER, userId)
        ).andExpectAll(
                status().isOk()
        );
    }


    private void removeUserPhotoSecondTime_ReturnsNotFound(long userId, UUID photoId) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(ServiceURI.DELETE_PHOTO.formatted(photoId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId)
        ).andExpectAll(
                status().isNotFound()
        );
    }

    private void removeUserPhoto_ReturnsOk(long userId, UUID photoId) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(ServiceURI.DELETE_PHOTO.formatted(photoId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void notValidUserRemovePhoto_ReturnsForbidden(long userId, UUID photoId) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(ServiceURI.DELETE_PHOTO.formatted(photoId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    private UUID addUserPhoto_ReturnsOk(long userId) throws Exception {
        byte[] fileContent = "testContent".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, fileContent);

        String content = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, ServiceURI.ADD_PHOTO)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(USER_HEADER, userId)
        ).andExpectAll(
                status().is2xxSuccessful()
        ).andReturn().getResponse().getContentAsString();
        return jsonService.extract(content, UUID.class);
    }

    private void setUserPhoto_ReturnsOk(long userId) throws Exception {
        byte[] content = "testContent".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, content);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, ServiceURI.SET_PHOTO)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(USER_HEADER, userId)
        ).andExpectAll(
                status().is2xxSuccessful()
        );
    }

    private void notValidParamsGetAllUsers_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_ALL_USERS)
                        .param("page", "-10")
                        .param("size", "-10")
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    private void emptyUserHeaderGetAllUsers_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_ALL_USERS)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void getAllUsers_ReturnsOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ServiceURI.GET_ALL_USERS)
                        .header(USER_HEADER, DEFAULT_USER)
        ).andExpectAll(
                status().isOk()
        );
    }

    private void notValidEditTest_ReturnsBadRequest(final long userId) throws Exception {
        EditUserRequest request = EditUserRequest.builder()
                .name("nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                        "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
                .build();
        mockMvc.perform(
                MockMvcRequestBuilders.put(ServiceURI.EDIT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId)
                        .content(jsonService.toJson(request))
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    private void validUserEditTest_ReturnsOk(final long userId) throws Exception {
        EditUserRequest request = EditUserRequest.builder()
                .name("name")
                .bio("bio")
                .birthday(Instant.parse("1980-04-09T10:15:30Z"))
                .isPublicAccount(false)
                .nickname("nickname")
                .location("USA")
                .build();

        editUser(userId, request);

        UserResponse response = jsonService.extract(getUser(userId).getContentAsString(), UserResponse.class);

        assertEquals(request.name(), response.name());
        assertEquals(request.bio(), response.bio());
        assertEquals(request.birthday(), response.birthday());
        assertEquals(request.isPublicAccount(), response.isPublicAccount());
        assertEquals(request.nickname(), response.nickname());
        assertEquals(request.location(), response.location());
    }


    private void userRegistration_ReturnsCreated() throws Exception {
        userRegistration(defaultRegistrationRequest);
    }

    private void userRegistrationWithOccupiedEmail_ReturnsConflict() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.USER_REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(defaultRegistrationRequest))
        ).andExpectAll(
                status().isConflict()
        );
    }

    private void userRegistrationEmptyBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.USER_REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(UserRegistrationRequest.builder().build()))
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    private Long userRegistration(UserRegistrationRequest request) throws Exception {
        String response = mockMvc.perform(
                MockMvcRequestBuilders.post(ServiceURI.USER_REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.toJson(request))
        ).andExpectAll(
                status().isCreated()
        ).andReturn().getResponse().getContentAsString();

        return Long.parseLong(jsonService.extract(response, "id"));
    }

    private MockHttpServletResponse getUser(final long userId) throws Exception {
        return mockMvc.perform(
                        MockMvcRequestBuilders.get(ServiceURI.GET_USER_BY_ID.formatted(userId))
                                .header(USER_HEADER, userId)
                ).andExpectAll(status().isOk())
                .andReturn().getResponse();
    }

    private void editUser(long userId, EditUserRequest request) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put(ServiceURI.EDIT_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId)
                        .content(jsonService.toJson(request))
        ).andExpectAll(
                status().isOk()
        );
    }

}
