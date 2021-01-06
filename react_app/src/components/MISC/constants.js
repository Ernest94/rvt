const prod = {
  url: {
    API_URL: 'https://voortgang.educom.nu:8081'
  }
};

const dev = {
  url: {
    API_URL: 'http://localhost:8081'
  }
};

export const config = process.env.NODE_ENV === 'development' ? dev : prod;
