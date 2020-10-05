export const constraints = {
    email: {
        presence: {
            allowEmpty: false,
            message: "^Een email is verplicht."
        },
        email: {
            message: "^Geef een geldig email adres."
        }
    },
    password: {
        presence: {
            allowEmpty: false,
            message: "^Een wachtwoord is verplicht."
        },
        length: {
            minimum: 4,
            tooShort: "^Wachtwoord heeft minimaal %{count} tekens nodig.",
            maximum: 20,
            tooLong: "^Wachtwoord heeft teveel tekens. Maximum is %{count} tekens."
        }
    },
};

export default constraints;