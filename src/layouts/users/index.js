// @mui material components
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import DataTable from "examples/Tables/DataTable";

// Data
import usersTableData from "layouts/users/data/usersTableData";

import { useNavigate } from "react-router-dom";

function Users() {
  const navigate = useNavigate();
  const { columns: uColumns, rows: uRows } = usersTableData();

  const handleEnrollNewUser = () => {
    navigate("/enroll-user");
  };

  return (
    <DashboardLayout>
      {/* <DashboardNavbar /> */}
      <MDBox pt={6} pb={3}>
        <Grid container spacing={6}>
          <Grid item xs={12}>
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
                display="flex"
                justifyContent="space-between" // Adjust this line
                alignItems="center" // Add this line to align items vertically
              >
                <MDTypography variant="h5" color="white">
                  Users
                </MDTypography>
                <MDButton onClick={handleEnrollNewUser} variant="gradient" color="success">
                  Enroll new user
                </MDButton>
              </MDBox>
              <MDBox pt={3}>
                <DataTable
                  table={{ columns: uColumns, rows: uRows }}
                  isSorted={true}
                  entriesPerPage={true}
                  showTotalEntries={true}
                  noEndBorder
                />
              </MDBox>
            </Card>
          </Grid>
        </Grid>
      </MDBox>
    </DashboardLayout>
  );
}

export default Users;
