class Permissions{

    static isUserAdmin() {
        return sessionStorage.getItem("userRole") === "Admin";
    }    
    static isUserTrainee() {
        return sessionStorage.getItem("userRole") === "Trainee";
    }    
    static isUserDocent() {
        return sessionStorage.getItem("userRole") === "Docent";
    }    
    static isUserSales() {
        return sessionStorage.getItem("userRole") === "Sales";
    }
    static isUserOffice() {
        return sessionStorage.getItem("userRole") === "Office";
    }

    //PERMISSIONS TO SEE MENU LINKS
    static canAddUser() {
        return (this.isUserAdmin()||this.isUserOffice()||this.isUserDocent());
    }
    static canSearch() {
        return (!this.isUserTrainee());
    }
    static canAddTheme() {
        return (this.isUserAdmin());
    }
    static canAddConcept() {
        return (this.isUserAdmin()||this.isUserDocent());
    }
    static canAddLocation() {
        return (this.isUserAdmin());
    }
    static canSeeConceptOverview() {
        return (this.isUserAdmin()||this.isUserDocent());
    }
    static canSeeOwnReview() {
        return (this.isUserTrainee());
    }
    static canAddReview() {
        return (this.isUserAdmin()||this.isUserDocent());
    }

    //PERMISSIONS TO VIEW PAGES
    static canViewDossier(dossierId) {
        if(sessionStorage.getItem("userId")===dossierId){
            return true;
        }
        else{
            return (!this.isUserTrainee());
        }
    }
    
    static canSeeReview() {
        return (this.isUserAdmin()||this.isUserDocent()||this.isUserTrainee()||this.isUserSales()||this.isUserOffice());
    }
    static canAddBundle() {
        return (this.isUserDocent()||this.isUserAdmin());
    }
}

export default Permissions;