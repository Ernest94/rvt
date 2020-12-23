import React from 'react';
import { validate } from 'validate.js';
import moment from 'moment';

class Utils{

    static setErrors = (errors) => {
        const foundErrors = Object.keys(errors).map((key) =>
            <li key={key}>{errors[key][0]}</li>
        );
        return foundErrors;
    }

    static compareLocations(first, second) {
      let bool = false;
      if (first.length === 0 || second.length === 0) return bool;
      Object.values(first).map(o1 => {
          Object.values(second).map(o2 => {
              if (o1.id === o2.id) {
                  bool = true;
              }
          });
      });
      return bool;
  }

    static dateValidation = () => {
        validate.extend(validate.validators.datetime, {
          // The value is guaranteed not to be null or undefined but otherwise it
          // could be anything.
          parse: function(value, options) {
            return +moment.utc(value);
          },
          // Input is a unix timestamp
          format: function(value, options) {
            var format = options.dateOnly ? "YYYY-MM-DD" : "YYYY-MM-DD hh:mm:ss";
            return moment.utc(value).format(format);
          }
        });
    }
}
export default Utils;