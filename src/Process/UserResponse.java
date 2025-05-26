
package Process;


public class UserResponse {
         private boolean status;
        private int accID;
        private String role;

        public UserResponse(boolean status, int accID, String role) {
            this.status = status;
            this.accID = accID;
            this.role = role;
        }

        public UserResponse() {
        }
        
        public boolean isStatus() {
            return status;
        }

        public int getAccID() {
            return accID;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public void setAccID(int accID) {
            this.accID = accID;
        }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
        
}
