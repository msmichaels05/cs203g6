import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { TextField, Button, Box, MenuItem, FormControl, InputLabel, Select } from "@mui/material";
import MDBox from "components/MDBox";
import MDButton from "components/MDButton";
import MDTypography from "components/MDTypography";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import Card from "@mui/material/Card";

const DetailsLog = () => {
  const { logid: logId } = useParams();
  const [log, setLog] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const logs = JSON.parse(localStorage.getItem("logs")) || [];
    const foundLog = logs.find((log) => `${log.logId}` === logId);
    setLog(foundLog || "not found");
  }, [logId]);

  if (log === undefined) return <div>Log Not Found</div>;
  if (!log) return <div>Loading...</div>;

  const handleBackClick = () => {
    navigate(-1);
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
                <MDTypography variant="h6" color="white">
                  Logs Details 
                </MDTypography>
              </MDBox>
          <MDBox p={3} display="flex" flexDirection="column">
            <MDBox mt={2} display="flex" flexDirection="column" alignItems="start">
              <MDTypography variant="h5" color="text" mt={1}>
                <strong>Log ID:</strong> {log.logId}
              </MDTypography>
              <MDTypography variant="body2" color="text" mt={1}>
                <strong>Timestamp:</strong> {new Date(log.timeOfAction).toLocaleString()}
              </MDTypography>
              <MDTypography variant="body2" color="text" mt={1}>
                <strong>Action:</strong> {log.actionTaken}
              </MDTypography>
              <MDTypography variant="body2" color="text" mt={1}>
                <strong>Admin ID:</strong> {log.adminId}
              </MDTypography>
              <MDTypography variant="body2" color="text" mt={1}>
                <strong>Details:</strong> {log.details || "-"}
              </MDTypography>
              <MDTypography variant="body2" color="text" mt={1}>
                <strong>User Agent:</strong> {log.userAgent}
              </MDTypography>
              <MDTypography variant="body2" color="text" mt={1}>
                <strong>Approx. Location:</strong> {log.approximateLocation || "-"}
              </MDTypography>
            </MDBox>
            <MDBox display="flex" justifyContent="space-between" alignItems="center" width="100%" mt={8}>
              <MDButton onClick={handleBackClick} variant="contained" color="info">
                Back
              </MDButton>
            </MDBox>
          </MDBox>
        </Card>
      </MDBox>
    </DashboardLayout>
  );

};
export default DetailsLog;