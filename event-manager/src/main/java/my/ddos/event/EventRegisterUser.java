package my.ddos.event;

public record EventRegisterUser(String username,
                                String fullName,
                                String password,
                                String role
) {
}
