import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Card, TextField, Button, Box, InputAdornment } from "@mui/material";
import MDBox from "components/MDBox";
import MDButton from "components/MDButton";
import MDTypography from "components/MDTypography";
import useAxiosPrivate from 'hooks/AxiosPrivate';
import MDSnackbar from "components/MDSnackbar";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import Divider from "@mui/material/Divider";

const AdjustPoints = () => {
  const { id: userId } = useParams();
  const [points, setPoints] = useState(0); // This will hold the current points
  const [pointsId, setPointsId] = useState(0);
  const [adjustment, setAdjustment] = useState(0); // This will hold the adjustment value
  const [successSB, setSuccessSB] = useState(false);

  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  const handleBackClick = () => {
    navigate(-1);
  };

  // Function to update the points based on the button clicked
  const adjustPoints = (value) => {
    setAdjustment(adjustment + value);
  };



  const handleDecrement = () => {
    setAdjustment(prevAdjustment => prevAdjustment - 1);
  };

  const handleIncrement = () => {
    setAdjustment(prevAdjustment => prevAdjustment + 1);
  };

  // Fetch user points data
  useEffect(() => {
    let isMounted = true;
    const fetchUser = async () => {
      try {
        const response = await axiosPrivate.get(`points/points`, { params: { userId } })
        console.log(response)
        if (isMounted) {
          setPoints(response.data[0].balance); // Assuming the response has a `balance` property
          setPointsId(response.data[0].id);
        }
      } catch (error) {
        console.error("Failed to fetch points", error);
      }
    };
    fetchUser();
    return () => { isMounted = false; }; // Cleanup function to prevent setting state on unmounted component
  }, [userId, axiosPrivate]);

  // Submit the new points to the backend
  const handleSubmit = async () => {
    try {
      const newBalance = points + adjustment; // Calculate the new balance
      const response = await axiosPrivate.post(`points/points/${pointsId}`, {
        new_balance: newBalance
      });
      setSuccessSB(true);
      setTimeout(() => {
        navigate("/users");
      }, 3000); // Redirect after showing success message
    } catch (error) {
      console.error("Failed to adjust points", error);
    }
  };

  // Handle change from the TextField directly (optional)
  const handlePointsChange = (e) => {
    setPoints(Number(e.target.value));
  };

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
              Adjust Points Balance
            </MDTypography>
          </MDBox>
          <MDBox p={3} display="flex" flexDirection="column" alignItems="center">
            <MDBox pt={3} pb={1} display="flex" flexDirection="column" alignItems="center" >
              {/* <MDTypography variant="h5" color="dark">Adjust Points Balance</MDTypography> */}
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <MDTypography
                  variant="h5"
                  color="dark"
                  sx={{
                    p: 2,
                    bgcolor: 'background.paper',
                    borderRadius: '8px',
                    // boxShadow: '0 4px 10px 0 rgba(0,0,0,0.1)',
                    width: 'fit-content',
                    mx: 'auto',
                    marginRight: '10px',
                    position: 'relative',
                  }}
                >
                  Current Points: {points}
                </MDTypography>
                {/* <span style={{ fontSize: '24px', margin: '0 12px' , alignSelf: 'center' }}>+</span> */}

                {/* <span style={{ fontSize: '24px', margin: '0 10px' , alignSelf: 'center' }}>➔</span> */}
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Button color="white" sx={{ fontSize: '24px' }} onClick={handleDecrement}>
                  -
                </Button>
                <MDTypography
                  variant="h3"
                  color="dark"
                  sx={{
                    p: 3,
                    paddingLeft: 8,
                    paddingRight: 8,
                    bgcolor: 'background.paper',
                    borderRadius: '8px',
                    // boxShadow: '0 4px 10px 0 rgba(0,0,0,0.1)',
                    border: '1px solid rgba(0, 0, 0, 0.1)', // Border property added
                    width: 'fit-content',
                    mx: 'auto',
                    marginRight: '10px',
                    marginLeft: '10px',
                    position: 'relative',
                  }}
                >
                  {adjustment}
                </MDTypography>
                <Button color="white" sx={{ fontSize: '24px' }}  onClick={handleIncrement}>
                  +
                </Button>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-evenly', flexWrap: 'wrap', gap: 2, mb: 1, mt: 4, width: '100%', pt: 2, pb: 2, }}>
                <Button variant="contained" color="grey" onClick={() => adjustPoints(-100)}>-100</Button>
                <Button variant="contained" color="white" onClick={() => adjustPoints(-50)}>-50</Button>
                <Button variant="contained" color="white" onClick={() => adjustPoints(-25)}>-25</Button>
                <Button variant="contained" color="white" onClick={() => adjustPoints(25)}>+25</Button>
                <Button variant="contained" color="white" onClick={() => adjustPoints(50)}>+50</Button>
                <Button variant="contained" color="white" onClick={() => adjustPoints(100)}>+100</Button>
              </Box>
              <Divider style={{ margin: '20px 0', width: '100%' }} />
              <MDTypography
                variant="h6"
                color="dark"
                sx={{
                  p: 2,
                  bgcolor: 'background.paper',
                  borderRadius: '8px',
                  boxShadow: '0 4px 10px 0 rgba(0,0,0,0.1)',
                  width: 'fit-content',
                  mx: 'auto',
                  '&::before': {
                    content: '""',
                    display: 'block',
                    width: '30px',
                    height: '3px',
                    backgroundColor: 'primary.main',
                    borderRadius: '5px',
                    position: 'relative',
                    top: '-10px',
                    left: 'calc(50% - 15px)',
                  }
                }}
              >
                New Points: {points + adjustment}
              </MDTypography>
              <MDBox display="flex" justifyContent="space-between" alignItems="center" width="100%" mt={8}>
                <MDButton onClick={handleBackClick} variant="contained" color="info">
                  Back
                </MDButton>
              <MDButton onClick={handleSubmit} color="info" variant="contained">Submit</MDButton>
              </MDBox>
            </MDBox>
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

export default AdjustPoints;
