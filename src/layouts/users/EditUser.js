import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { TextField, Button, Box, MenuItem, FormControl, InputLabel, Select } from "@mui/material";
import MDBox from "components/MDBox";
import MDButton from "components/MDButton";
import MDTypography from "components/MDTypography";
import useAxiosPrivate from 'hooks/AxiosPrivate'
import MDSnackbar from "components/MDSnackbar";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import Card from "@mui/material/Card";

const EditUser = () => {
  const { id: userId } = useParams();
  const [user, setUser] = useState(null);
  const [successSBRequets, setSuccessSBRequest] = useState(false); // State to control Snackbar visibility
  const [successSB, setSuccessSB] = useState(false); // State to control Snackbar visibility
  // const [submitMethod, setSubmitMethod] = useState(null);

  const navigate = useNavigate();
  const axiosPrivate = useAxiosPrivate();

  const handleBackClick = () => {
    navigate(-1);
  };

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const founduser = await axiosPrivate.get(`user/user/${userId}`).then((res) => res.data);
        delete founduser.enroll_date;
        delete founduser.id;
        console.log(founduser);
        setUser(founduser);
      } catch (error) {
        console.error("Failed to fetch user", error);
      }
    }
    fetchUser();
  }, [userId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(user)
    try{
      if (user.role == "Engineer") {
        const response = await axiosPrivate.post(`user/request/user/${userId}`, user);
        setSuccessSBRequest(true);
      } else{
        const response = await axiosPrivate.put(`user/info/${userId}`, user);
        setSuccessSB(true);
      }
      setTimeout(() => {
        navigate("/users");
      }, 3000);
    } catch (error) {
      console.error("Failed to edit user", error);
    }
  };


  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  if (!user) return <div></div>;

  return (
    <DashboardLayout>
      <MDBox pt={6} pb={3}>
        <Card>
          <MDBox
            mx={2}
            mt={-3}
            py={3}
            px={2}
            variant="gradient"
            bgColor="info"
            borderRadius="lg"
            coloredShadow="info"
          >
            <MDTypography variant="h5" color="white">
              Edit User
            </MDTypography>
          </MDBox>
          <MDBox p={3} display="flex" flexDirection="column" alignItems="start">
            <form onSubmit={handleSubmit} style={{ width: "100%" }}>
              <TextField
                fullWidth
                name="first_name"
                label="First Name"
                value={user.first_name || ""}
                onChange={handleChange}
                margin="normal"
              />
              <TextField
                fullWidth
                name="last_name"
                label="Last Name"
                value={user.last_name || ""}
                onChange={handleChange}
                margin="normal"
              />
              <TextField
                fullWidth
                name="email"
                label="Email"
                value={user.email || ""}
                onChange={handleChange}
                margin="normal"
              />
              <FormControl fullWidth margin="normal">
                <InputLabel>Role</InputLabel>
                <Select
                  name="role"
                  value={user.role || ""}
                  onChange={handleChange}
                  label="Role"
                  sx={{
                    height: "3.4375em",
                    ".MuiOutlinedInput-notchedOutline": {
                      borderColor: "rgba(0, 0, 0, 0.23)",
                    },
                    ".MuiSelect-select": {
                      paddingTop: "10px",
                      paddingBottom: "10px",
                    },
                  }}
                >
                  <MenuItem value="OWNER">Admin</MenuItem>
                  <MenuItem value="MANAGER">Manager</MenuItem>
                  <MenuItem value="ENGINEER">Engineer</MenuItem>
                  <MenuItem value="PRODUCT_MANAGER">Product Manager</MenuItem>
                  <MenuItem value="USER">User</MenuItem>
                </Select>
              </FormControl>
              <MDBox display="flex" justifyContent="space-between" alignItems="center" width="100%" mt={8}>
                <MDButton onClick={handleBackClick} variant="contained" color="info">
                  Back
                </MDButton>
                <MDButton variant="outlined" color="info" type="submit">
                  Save Changes
                </MDButton>
              </MDBox>
            </form>
            <MDSnackbar
              color="success"
              icon="check"
              title="Material Dashboard"
              content="User has been successfully edited."
              dateTime="Just now"
              open={successSB}
              onClose={() => setSuccessSB(false)}
              close={() => setSuccessSB(false)}
              bgWhite
            />
          </MDBox>
        </Card>
      </MDBox>
    </DashboardLayout>
  );
};

export default EditUser;
