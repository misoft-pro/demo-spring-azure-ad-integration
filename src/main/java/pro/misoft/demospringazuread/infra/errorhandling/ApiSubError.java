package pro.misoft.demospringazuread.infra.errorhandling;

public record ApiSubError(
        String objectName,
        String fieldName,
        Object rejectedValue,
        String message) {
}
