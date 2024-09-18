import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { TextField, Button, Box, MenuItem, FormControl, InputLabel, Select } from "@mui/material";
import MDBox from "components/MDBox";
import MDButton from "components/MDButton";
import MDTypography from "components/MDTypography";
import useAxiosPrivate from 'hooks/AxiosPrivate'
import MDSnackbar from "components/MDSnackbar";

const DeleteUser = () => {
  const { id: userId } = useParams();
  const [user, setUser] = useState(null);
  const [successSB, setSuccessSB] = useState(false); // State to control Snackbar visibility

  const navigate = useNavigate();
  const axiosPrivate = useAxiosPrivate();


  
  useEffect(() => {
    const fetchUser = async()=>{
      try{
        const founduser = await axiosPrivate.get(`user/user/${userId}`).then((res) => res.data);
        delete founduser.enroll_date;
        delete founduser.id;
        if(founduser){
          setUser(founduser);
        }else{
          navigate("/users", { replace: true });
        }
      }catch(error){
        console.error("Failed to fetch user", error);
      }
    }     
    fetchUser();
  }, [userId,navigate]);
    

  const handleSubmit = async(e) => {
    e.preventDefault();
    try{
      const response = await axiosPrivate.delete(`user/user/${userId}`,user)
      setSuccessSB(true);
      setTimeout(() => {
        navigate("/users");
      }, 3000); 
    }catch(error){
      console.error("Failed to edit user", error);
    }
  };

  if (!user) {
    return <div>Loading user data...</div>;
  }

  return (
    <MDBox p={3} display="flex" flexDirection="column" alignItems="center">
      <form style={{ width: "50%" }} onSubmit={handleSubmit}>
        <MDTypography variant="h4" color="dark">
          Delete user
        </MDTypography>
        <TextField
          fullWidth
          name="firstName"
          label="First Name"
          value={user.firstName}
          margin="normal"
          disabled
        />
        <TextField
          fullWidth
          name="lastName"
          label="Last Name"
          value={user.lastName}
          margin="normal"
          disabled
        />
        <TextField
          fullWidth
          name="email"
          label="Email"
          value={user.email}
          margin="normal"
          disabled
        />
        <TextField fullWidth name="role" label="Role" value={user.role} margin="normal" disabled />
        <TextField
          fullWidth
          name="points"
          label="Points Balance"
          value={user.points}
          margin="normal"
          disabled
        />
        <MDBox mt={4} display="flex" justifyContent="center">
          <MDButton variant="outlined" color="error" type="submit">
            Delete user
          </MDButton>
        </MDBox>
      </form>
      <MDSnackbar
        color="success"
        icon="check"
        title="User deleted"
        content="User has been successfully deleted."
        dateTime="Just now"
        open={successSB}
        onClose={() => setSuccessSB(false)}
        close={() => setSuccessSB(false)}
        bgWhite
      />
    </MDBox>
  );
};

export default DeleteUser;
