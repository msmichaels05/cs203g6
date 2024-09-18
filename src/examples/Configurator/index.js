import { useState, useEffect } from "react";

// @mui material components
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import Icon from "@mui/material/Icon";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Custom styles for the Configurator
import ConfiguratorRoot from "examples/Configurator/ConfiguratorRoot";

// Material Dashboard 2 React context
import { useMaterialUIController, setOpenConfigurator } from "context";

function Configurator() {
  const [controller, dispatch] = useMaterialUIController();
  const { openConfigurator } = controller;

  const handleCloseConfigurator = () => setOpenConfigurator(dispatch, false);

  return (
    <ConfiguratorRoot variant="permanent" ownerState={{ openConfigurator }}>
      <MDBox
        display="flex"
        justifyContent="space-between"
        alignItems="baseline"
        pt={4}
        pb={0.5}
        px={3}
      >
        <MDBox>
          <MDTypography variant="h5">Configurator</MDTypography>
          <MDTypography variant="body2" color="text">
            Dashboard options.
          </MDTypography>
        </MDBox>

        <Icon
          sx={({ typography: { size }, palette: { dark, white } }) => ({
            fontSize: `${size.lg} !important`,
            color: dark.main,
            stroke: "currentColor",
            strokeWidth: "2px",
            cursor: "pointer",
            transform: "translateY(5px)",
          })}
          onClick={handleCloseConfigurator}
        >
          close
        </Icon>
      </MDBox>

      <Divider />

      <MDBox pt={3} pb={3} px={3}>
        {/* Additional configurable options can be added here */}
        <MDTypography variant="body1" color="text" mb={2}>
          Configure your dashboards options.
        </MDTypography>
      </MDBox>
    </ConfiguratorRoot>
  );
}

export default Configurator;
