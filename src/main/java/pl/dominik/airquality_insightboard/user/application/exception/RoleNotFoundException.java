package pl.dominik.airquality_insightboard.user.application.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String roleName) {
        super("Role '" + roleName + "' not found");
    }
}
