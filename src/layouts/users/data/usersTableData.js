/* eslint-disable react/prop-types */
/* eslint-disable react/function-component-definition */

import Icon from "@mui/material/Icon";
// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDAvatar from "components/MDAvatar";
import MDBadge from "components/MDBadge";
import MDButton from "components/MDButton";
import { Link } from "react-router-dom";
import React, { useEffect, useState } from 'react';
import useAxiosPrivate from 'hooks/AxiosPrivate'
import MDSnackbar from "components/MDSnackbar";
import { useNavigate, useParams } from "react-router-dom";


export default function data() {
  const [users, setUsers] = useState([]);
  const [successSB, setSuccessSB] = useState(false); // State to control Snackbar visibility
  const axiosPrivate = useAxiosPrivate(); // Use your custom hook here
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch users
        const response = await axiosPrivate.get('user/all_users');
        const users = response.data;
        
        // Fetch points for all users in parallel
        const usersWithPoints = await Promise.all(users.map(async (user) => {
          const points = await getPoints(user.id);
          return { ...user, points }; // Merge user with their points
        }));
  
        // Update state with users and their points
        setUsers(usersWithPoints);
      } catch (error) {
        console.error("Failed to fetch users or points", error);
        // Handle error appropriately
      }
    };
  
    fetchData();
  }, [axiosPrivate]);
  
  const getPoints = async (userId) => {
    try {
      const response = await axiosPrivate.get(`points/points`, { params: { userId } });
      if (response.data.length === 0) {
        return 0;
      }
      return response.data[0].balance;
    } catch (error) {
      console.error("Failed to fetch points for user ID:", userId, error);
      return 0; // Return 0 points in case of error
    }
  };

   // Function to check if points can be adjusted
   const canAdjustPoints = async (userId) => {
    try {
      const response = await axiosPrivate.get(`points/points`, { params: { userId } });
      console.log(response)
      return response.data[0].length !== 0;
    } catch (error) {
      console.log(error); // Log error
      return false; // Return false in case of error
    }
  };

  // Event handler for the Adjust Points button click
  const handleAdjustPointsClick = async (userId) => {
    const canAdjust = await canAdjustPoints(userId);
    if (canAdjust) {
      navigate(`/adjust-points/${userId}`);
    } else {
      setSuccessSB(true); // Show Snackbar if points cannot be adjusted
    }
  };

  const Actions = ({ userId }) => (
    <MDBox display="flex" alignItems="center">
      <Link to={`/edit-user/${userId}`}>
        <MDButton variant="outlined" color="info" size="small">
          <Icon>edit</Icon>&nbsp;Edit
        </MDButton>
      </Link>
      
      <MDButton variant="outlined" color="info" size="small" style={{ marginLeft: "8px" }} 
      onClick={()=>handleAdjustPointsClick(userId)}
      >
        <Icon>tune</Icon>
        &nbsp;Adjust Points
      </MDButton>
      
      <Link to={`/delete-user/${userId}`}>
        <MDButton variant="outlined" color="error" size="small" style={{ marginLeft: "8px" }}>
          <Icon>delete</Icon>
          &nbsp;Delete
        </MDButton>
      </Link>
      <MDSnackbar
              color="error"
              icon="warning"
              title="No points account"
              content="User has no points balance for you to adjust."
              dateTime="Just now"
              open={successSB}
              onClose={() => setSuccessSB(false)}
              close={() => setSuccessSB(false)}
              bgWhite
      />
    </MDBox>
  );

  const rows = users.map((user) => ({
    userid: (
      <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
        {user.id}
      </MDTypography>
    ),
    name: (
      <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
        {`${user.first_name} ${user.last_name}`}
      </MDTypography>
    ),
    email: (
      <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
        {user.email}
      </MDTypography>
    ),
    points: (
      <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
        {user.points}
      </MDTypography>
    ),
    role: (
      <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
        {user.role}
      </MDTypography>
    ),
    actions: <Actions userId={user.id} />,
  }));

  return {
    columns: [
      { Header: "user id", accessor: "userid", align: "left", width: "15%" },
      { Header: "name", accessor: "name", align: "left", width: "15%" },
      { Header: "email", accessor: "email", align: "center", width: "12%" },
      { Header: "points balance", accessor: "points", align: "center", width: "10%" },
      { Header: "role", accessor: "role", align: "center", width: "10%" },
      { Header: "actions", accessor: "actions", align: "center", width: "35%" },
    ],
    rows,
  };
}
