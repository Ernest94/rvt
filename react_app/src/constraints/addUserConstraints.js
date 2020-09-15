import moment from 'moment';

export const constraints = {
    name: {
      presence: {
          allowEmpty: false,
          message: "^Een naam is verplicht."
      } 
    },
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
    dateActive: {
        presence: {
            allowEmpty: false,
            message: "^Een datum is verplicht."
        },
        datetime: {
            dateOnly: true,
            earliest: moment().subtract(1, 'days'),
            message: "^Gebruiker kan pas vanaf vandaag actief worden."
        }
    }
};

export default constraints;