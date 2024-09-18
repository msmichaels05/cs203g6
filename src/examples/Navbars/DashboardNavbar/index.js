import { useState, useEffect } from "react";
import PropTypes from "prop-types";

// react-router components
import { useLocation, Link } from "react-router-dom";

// @mui/material components
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Icon from "@mui/material/Icon";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDInput from "components/MDInput";

// Material Dashboard 2 React example components
import Breadcrumbs from "examples/Breadcrumbs";

// Material Dashboard 2 React context
import { useMaterialUIController, setOpenConfigurator } from "context";

function DashboardNavbar({ absolute, isMini }) {
  const [navbarType, setNavbarType] = useState("static");
  const [controller, dispatch] = useMaterialUIController();
  const { miniSidenav } = controller;
  const route = useLocation().pathname.split("/").slice(1);
  const handleMiniSidenav = () => setMiniSidenav(dispatch, !miniSidenav);

  useEffect(() => {
    // Simplify by removing the scroll event listener related to changing navbar transparency
  }, [dispatch]);

  const handleConfiguratorOpen = () => setOpenConfigurator(dispatch, !openConfigurator);

  return (
    <AppBar position={absolute ? "absolute" : navbarType} >
      <Toolbar>
        <MDBox  m={0}>
          <Breadcrumbs icon="home" title={route[route.length - 1]} route={route} />
        </MDBox>
        {/* {isMini ? null : (
          <MDBox>
            <MDBox pr={1}>
              <MDInput label="Search here" />
            </MDBox>
            <MDBox color="inherit">
              <Link to="/authentication/sign-in/basic">
                <IconButton size="small" disableRipple>
                  <Icon>account_circle</Icon>
                </IconButton>
              </Link>
              <IconButton size="small" disableRipple color="inherit" onClick={handleMiniSidenav}>
                <Icon fontSize="medium">{miniSidenav ? "menu_open" : "menu"}</Icon>
              </IconButton>
              <IconButton
                size="small"
                disableRipple
                color="inherit"
                onClick={handleConfiguratorOpen}
              >
                <Icon>settings</Icon>
              </IconButton>
            </MDBox>
          </MDBox>
        )} */}
      </Toolbar>
    </AppBar>
  );
}

// Setting default values for the props of DashboardNavbar
DashboardNavbar.defaultProps = {
  absolute: false,
  isMini: false,
};

// Typechecking props for the DashboardNavbar
DashboardNavbar.propTypes = {
  absolute: PropTypes.bool,
  isMini: PropTypes.bool,
};

export default DashboardNavbar;
