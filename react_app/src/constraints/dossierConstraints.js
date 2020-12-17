export const constraints = {
    name: {
        presence: {
          allowEmpty: false,
          message: "^Een naam is verplicht."
        },
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
    role: {
        presence: {
            allowEmpty: false,
            message: "^Een rol is verplicht."
        }
    },
    currentLocationsIds: {
        presence: {
            allowEmpty: false,
            message: "^Een locatie is verplicht."
        }
    },
    startDate: {
        presence: {
            allowEmpty: false,
            message: "^Een datum is verplicht."
        },
        datetime: {
            dateOnly: true,
        }
    },
};

export default constraints;