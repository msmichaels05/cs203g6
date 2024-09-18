// prop-types is a library for typechecking of props
import PropTypes from "prop-types";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";

function DataTableBodyCell({ noBorder, align, children, width }) {
  return (
    <MDBox
      component="td"
      textAlign={align}
      py={1}
      px={2}
      width={width}
      sx={({ palette: { light }, typography: { size }, borders: { borderWidth } }) => ({
        fontSize: size.sm,
        borderBottom: noBorder ? "none" : `${borderWidth[1]} solid ${light.main}`,
      })}
    >
     <MDBox
        display="block" // Change to block to ensure scrolling works
        width="100%"
        sx={{ verticalAlign: "middle",}}
      >
        {children}
      </MDBox>
    </MDBox>
  );
}

// Setting default values for the props of DataTableBodyCell
DataTableBodyCell.defaultProps = {
  noBorder: false,
  align: "left",
};

// Typechecking props for the DataTableBodyCell
DataTableBodyCell.propTypes = {
  width: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  children: PropTypes.node.isRequired,
  noBorder: PropTypes.bool,
  align: PropTypes.oneOf(["left", "right", "center"]),
};

export default DataTableBodyCell;
