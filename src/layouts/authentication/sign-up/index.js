// react-router-dom components
import { Link, useNavigate } from "react-router-dom";

// @mui material components
import Card from "@mui/material/Card";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDInput from "components/MDInput";
import MDButton from "components/MDButton";

// Authentication layout components
import CoverLayout from "layouts/authentication/components/CoverLayout";

// Images
import bgImage from "assets/images/bg-sign-up-cover.jpeg";

import UserPool from "../../../UserPool";
import React, { useState } from "react";

function Cover() {
  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  const onSubmit = (event) => {
    event.preventDefault();
    setError("");

    UserPool.signUp(username, password, [{ Name: 'email', Value: email }], null, (err, data) => {
      if (err) {
        console.error(err);
        setError(err.message);
      } else {
        // console.log(data);
        navigate("../sign-in");
      }
    });
  };

  return (
    <CoverLayout image={bgImage}>
      <Card>
        <MDBox
          variant="gradient"
          bgColor="info"
          borderRadius="lg"
          coloredShadow="success"
          mx={2}
          mt={-3}
          p={3}
          mb={1}
          textAlign="center"
        >
          <MDTypography variant="h4" fontWeight="medium" color="white" mt={1}>
            Join us today
          </MDTypography>
          <MDTypography display="block" variant="button" color="white" my={1}>
            Enter your email and password to register
          </MDTypography>
        </MDBox>
        <MDBox pt={4} pb={3} px={3}>
          <form onSubmit={onSubmit}>
            {error && (
              <MDBox mb={2}>
                <MDTypography variant="caption" color="error">
                  {error}
                </MDTypography>
              </MDBox>
            )}
            <MDBox>
              <MDBox mb={2}>
                <MDInput type="text" label="Username" variant="standard" fullWidth value={username} onChange={(e) => setUsername(e.target.value)} />
              </MDBox>
              <MDBox mb={2}>
                <MDInput type="email" label="Email" variant="standard" fullWidth value={email} onChange={(e) => setEmail(e.target.value)} />
              </MDBox>
              <MDBox mb={2}>
                <MDInput type="text" label="Name" variant="standard" fullWidth value={name} onChange={(e) => setName(e.target.value)} />
              </MDBox>
              <MDBox mb={2}>
                <MDInput type="password" label="Password" variant="standard" fullWidth value={password} onChange={(e) => setPassword(e.target.value)} />
              </MDBox>
              <MDBox mt={4} mb={1}>
                <MDButton type="submit" variant="gradient" color="info" fullWidth>
                  Register
                </MDButton>
              </MDBox>
              <MDBox mt={3} mb={1} textAlign="center">
                <MDTypography variant="button" color="text">
                  Already have an account?{" "}
                  <MDTypography
                    component={Link}
                    to="/authentication/sign-in"
                    variant="button"
                    color="info"
                    fontWeight="medium"
                    textGradient
                  >
                    Sign In
                  </MDTypography>
                </MDTypography>
              </MDBox>
            </MDBox>
          </form>
        </MDBox>
      </Card>
    </CoverLayout>
  );
}

export default Cover;
