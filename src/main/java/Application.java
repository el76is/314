import model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class Application {

    private final String url = "http://91.241.64.178:7081/api/users";
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    static StringBuilder result = new StringBuilder();

    public Application() {
        String sessionId = getAllUsers();
        headers.set("cookie", sessionId);
    }

    public static void main(String[] args) {

        Application app = new Application();
        app.createUser();
        app.updateUser();
        app.deleteUser(3L);
        System.out.println(result);
    }

    public String getAllUsers() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        return String.join(";", Objects.requireNonNull(forEntity.getHeaders().get("set-cookie")));
    }

    public void createUser() {
        User user = new User("James","Brown", (byte) 73);
        user.setId(3L);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        String request = restTemplate.postForEntity(url, entity, String.class).getBody();
        result.append(request);
        new ResponseEntity<>(request, HttpStatus.OK);
    }

    public void updateUser() {
        User user = new User("Thomas","Shelby", (byte) 45);
        user.setId(3L);
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        String request = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
        result.append(request);
        new ResponseEntity<>(request, HttpStatus.OK);
    }

    public void deleteUser(@PathVariable Long id) {
        HttpEntity<User> entity = new HttpEntity<>(headers);
        String request = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, entity, String.class).getBody();
        result.append(request);
        new ResponseEntity<>(request, HttpStatus.OK);
    }
}
