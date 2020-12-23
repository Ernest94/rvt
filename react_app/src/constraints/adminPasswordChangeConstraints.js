export const constraints = {
    newPassword: {
        presence: {
            allowEmpty: false,
            message: "^Een nieuw wachtwoord is verplicht."
        },
        length: {
            minimum: 4,
            tooShort: "^Nieuw wachtwoord heeft minimaal %{count} tekens nodig.",
            maximum: 20,
            tooLong: "^Nieuw wachtwoord heeft teveel tekens. Maximum is %{count} tekens."
        }
    },
    repeatPassword: {
        presence: {
            allowEmpty: false,
            message: "^Het herhaalde wachtwoord is verplicht."
        },
        length: {
            minimum: 4,
            tooShort: "^Herhaald wachtwoord heeft minimaal %{count} tekens nodig.",
            maximum: 20,
            tooLong: "^Herhaald wachtwoord heeft teveel tekens. Maximum is %{count} tekens."
        },
        equality: {
            attribute: "newPassword",
            message: "^De nieuwe wachtwoorden komen niet overeen." 
        }
    },
};

export default constraints;