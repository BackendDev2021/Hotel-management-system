package com.dolphinskart.bulkinjestion.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.dolphinskart.bulkinjestion.model.Dimensions;
import com.dolphinskart.bulkinjestion.model.DisplayAttributes;
import com.dolphinskart.bulkinjestion.model.DolphinSKU;
import com.dolphinskart.bulkinjestion.model.DolphinsVariants;
import com.dolphinskart.bulkinjestion.model.ExcelColRange;
import com.dolphinskart.bulkinjestion.model.ExcelData;
import com.dolphinskart.bulkinjestion.model.Images;
import com.dolphinskart.bulkinjestion.model.InputFile;
import com.dolphinskart.bulkinjestion.model.InstallationAndDemoServices;
import com.dolphinskart.bulkinjestion.model.PackageDetails;
import com.dolphinskart.bulkinjestion.model.PickupDiscount;
import com.dolphinskart.bulkinjestion.model.Price;
import com.dolphinskart.bulkinjestion.model.Product;
import com.dolphinskart.bulkinjestion.model.ReturnPolicy;
import com.dolphinskart.bulkinjestion.model.ShippingDetails;

@Service
public class BulkInjestionServiceImpl implements BulkInjestionService {

	@Override
	public List<ExcelData> createFromFile(InputFile fileInput) {
		List<ExcelData> productDetailsList = new ArrayList<>();
		try {
			File productInputFile = new File(fileInput.getFile().getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(productInputFile);
			fos.write(fileInput.getFile().getBytes());
			fos.close();
			Workbook workbook = WorkbookFactory.create(productInputFile);
			// provide which sheet to fetch data
			Sheet sheet = workbook.getSheetAt(0);
			Row firstHeaderRow = sheet.getRow(0);
			// In this map first row, ranges of columns are stored
			Map<String, ExcelColRange> excelFirstHeaderColRange = new HashMap<>();
			if (ObjectUtils.isNotEmpty(firstHeaderRow)) {
				for (int j = firstHeaderRow.getFirstCellNum(); j <= firstHeaderRow.getLastCellNum(); j++) {
					Cell ce = firstHeaderRow.getCell(j);
					if (validateCellStringNumeric(ce)) {
						int columnIndex = ce.getColumnIndex();
						int nextColIndex = findNextColIndex(firstHeaderRow, columnIndex);
						excelFirstHeaderColRange.put(ce.getStringCellValue(),
								new ExcelColRange(columnIndex, nextColIndex - 1));
					}
				}
			}
			// column range for display attributes as they are dynamic
			ExcelColRange excelColRangeDisplayAttributes = excelFirstHeaderColRange.get("Display Attribute");
			// column range for images as they are dynamic
			ExcelColRange excelColRangeImages = excelFirstHeaderColRange.get("Images");
			// column range for bullet points as they are dynamic
			ExcelColRange excelColRangeBulletPts = excelFirstHeaderColRange.get("Bullet points");
			// as real data entered from row 2, row iteration starts from 2
			for (int i = sheet.getFirstRowNum() + 2; i <= sheet.getLastRowNum(); i++) {
				Row subHeaderRow = sheet.getRow(1);
				ExcelData excelData = new ExcelData();
				Product productED = new Product();
				DolphinsVariants dolphinsVariantsED = new DolphinsVariants();
				DolphinSKU dolphinSKUED = new DolphinSKU();
				Price price = new Price();
				PackageDetails packageDetails = new PackageDetails();
				ShippingDetails shippingDetails = new ShippingDetails();
				Row presentRow = sheet.getRow(i);
				for (int j = presentRow.getFirstCellNum(); j < presentRow.getLastCellNum(); j++) {
					String subHeaderValue = subHeaderRow.getCell(j).getStringCellValue();
					Cell ce = presentRow.getCell(j);
					if (validateCellStringNumeric(ce)) {
						if (subHeaderValue.equals("Status")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								excelData.setStatus(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("categoryId")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								productED.setCategoryId((int) ce.getNumericCellValue());
							}
						} else if (subHeaderValue.equals("subCategoryId")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								productED.setSubCategoryId((int) ce.getNumericCellValue());
							}
						} else if (subHeaderValue.equals("category")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								productED.setCategory(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("subCategory")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								productED.setSubCategory(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("Product Name")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								productED.setProductName(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("Manufacturer")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								productED.setManufacturer(ce.getStringCellValue());
							}
						}
						// check for number also
						else if (subHeaderValue.equals("Model")) {
							productED.setModel(numericSafeString(ce));
						} else if (subHeaderValue.equals("Group ID")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								excelData.setGroupId((int) ce.getNumericCellValue());
							}
						} else if (subHeaderValue.equals("brand")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								productED.setBrand(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("productType")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								productED.setProductType(ce.getStringCellValue());
							}
						}
						// few values can be numeric also
						else if (j >= excelColRangeDisplayAttributes.getMinColRange()
								&& j <= excelColRangeDisplayAttributes.getMaxColRange()) {
							if (CollectionUtils.isNotEmpty(dolphinsVariantsED.getDisplayAttributes())) {
								// check for value type in excel maybe number
								dolphinsVariantsED.getDisplayAttributes()
										.add(new DisplayAttributes(CaseUtils.toCamelCase(subHeaderValue, false, ' '),
												subHeaderValue, numericSafeString(ce)));
							} else {
								List<DisplayAttributes> displayAttributesList = new ArrayList<>();
								displayAttributesList
										.add(new DisplayAttributes(CaseUtils.toCamelCase(subHeaderValue, false, ' '),
												subHeaderValue, numericSafeString(ce)));
								dolphinsVariantsED.setDisplayAttributes(displayAttributesList);
							}
						} else if (j >= excelColRangeImages.getMinColRange()
								&& j <= excelColRangeImages.getMaxColRange()) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								if (CollectionUtils.isNotEmpty(dolphinsVariantsED.getImages())) {

									dolphinsVariantsED.getImages().add(new Images(ce.getStringCellValue()));
								} else {
									List<Images> imgList = new ArrayList<>();
									imgList.add(new Images(ce.getStringCellValue()));
									dolphinsVariantsED.setImages(imgList);
								}
							}
						}
//						else if (j >= excelColRangeBulletPts.getMinColRange()
//								&& j <= excelColRangeBulletPts.getMaxColRange()) {
//							if ((ce.getCellType().equals(CellType.STRING))) {
//								if (StringUtils.isNotBlank(dolphinsVariantsED.getBulletPoints())) {
//
//									dolphinsVariantsED.getImages().add(new Images(ce.getStringCellValue()));
//								} else {
//									List<Images> imgList = new ArrayList<>();
//									imgList.add(new Images(ce.getStringCellValue()));
//									dolphinsVariantsED.setImages(imgList);
//								}
//							}
//						}
						// check for number also
						else if (subHeaderValue.equals("EAN")) {

							dolphinsVariantsED.setEAN(numericSafeString(ce));

						}
						// check for number also
						else if (subHeaderValue.equals("UPC")) {

							dolphinsVariantsED.setUPC(numericSafeString(ce));

						}
						// check for number also
						else if (subHeaderValue.equals("ISBN")) {

							dolphinsVariantsED.setISBN(numericSafeString(ce));

						}
						// check for number also
						else if (subHeaderValue.equals("MPN")) {

							dolphinsVariantsED.setMPN(numericSafeString(ce));

						} else if (subHeaderValue.equals("Title")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								dolphinsVariantsED.setTitle(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("Description")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								dolphinsVariantsED.setDescription(ce.getStringCellValue());
							}
						}

						else if (subHeaderValue.equals("Condition of the Product")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								dolphinSKUED.setCondition(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("Common Name of the Product")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								dolphinSKUED.setCommonName(ce.getStringCellValue());
							}
						} else if (subHeaderValue.equals("COD Available?")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								boolean codAvailable = (StringUtils.isNotBlank(ce.getStringCellValue())
										&& StringUtils.equalsIgnoreCase("Yes", ce.getStringCellValue())) ? true : false;
								dolphinSKUED.setCashOnDelivery(codAvailable);
							}
						}
						// check for number also
						else if (subHeaderValue.equals("Seller SKU")) {

							dolphinSKUED.setSellerSkuId(numericSafeString(ce));

						} else if (subHeaderValue.equals("MRP")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								price.setMrp(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("MOP")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								price.setMop(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("Target Price")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								price.setSellingPrice(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("Selling Price")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								price.setFinalSellingPrice(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						}
						// number
						else if (subHeaderValue.equals("HSN Code")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setHSN(Math.round(ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("Tax %")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setTaxPercent(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("Fulfillment Type: Yes/No")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								dolphinSKUED.setFulfilledBy((StringUtils.isNotBlank(ce.getStringCellValue())
										&& StringUtils.equalsIgnoreCase("Yes", ce.getStringCellValue())) ? "0" : "1");
							}
						} else if (subHeaderValue.equals("Maximum Quantity Orderable")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setMaxQuantityOrder(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("Minimum Quantity Orderable")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setMinQuantityOrder(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("Available Stock")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setStock(ce.getNumericCellValue());
							}
						} else if (subHeaderValue.equals("Surplus stock")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setSurplusStock(ce.getNumericCellValue());
							}
						} else if (subHeaderValue.equals("Re-stock Quantity")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setRestock(ce.getNumericCellValue());
							}
						} else if (subHeaderValue.equals("Next Restock Date")) {
							if ((ce.getCellType().equals(CellType.STRING))
									&& StringUtils.isNotBlank(ce.getStringCellValue())) {
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
								String reStockDateStr = ce.getStringCellValue();
								LocalDate reStockDate = LocalDate.parse(reStockDateStr, formatter);
								dolphinSKUED.setRestockDate(reStockDate);
							}
						} else if (subHeaderValue.equals("Package Weight (in kgs)")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								packageDetails.setWeight(new Dimensions("kg", BigDecimal
										.valueOf(ce.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP)));
							}
						}
//						else if (subHeaderValue.equals("Dimensions ( LxWxH in cm)")) {
//							if ((ce.getCellType().equals(CellType.STRING))) {
//								String dimensions = ce.getStringCellValue();
//								String[] dimensionsList = dimensions.split("x");
//								String length = dimensionsList[0];
//								String width = dimensionsList[1];
//								String height = dimensionsList[2];
//								packageDetails.setLength(new Dimensions("kg",
//										BigDecimal.valueOf(Double.valueOf(length)).setScale(2, RoundingMode.HALF_UP)));
//								packageDetails.setWidth(new Dimensions("kg",
//										BigDecimal.valueOf(Double.valueOf(width)).setScale(2, RoundingMode.HALF_UP)));
//								packageDetails.setHeight(new Dimensions("kg",
//										BigDecimal.valueOf(Double.valueOf(height)).setScale(2, RoundingMode.HALF_UP)));
//							}
//						} 
						else if (subHeaderValue.equals("GiftWrap Available: Yes/No")) {
							if ((ce.getCellType().equals(CellType.STRING)
									&& StringUtils.isNotBlank(ce.getStringCellValue())
									&& StringUtils.equalsIgnoreCase("No", ce.getStringCellValue()))) {
								packageDetails.setGiftWrapAvailable(false);
							} else if (ce.getCellType().equals(CellType.NUMERIC)) {
								packageDetails.setGiftWrapAvailable(true);
								packageDetails.setGiftWrapAmount(convertDoubleToBigDecimal(ce.getNumericCellValue()));
							}
						}
						// check for enum
						else if (subHeaderValue.equals("Self / Dolphins Shipping")) {
							if ((ce.getCellType().equals(CellType.STRING))) {
								String shippingType = (StringUtils.isNotBlank(ce.getStringCellValue())
										&& StringUtils.equalsIgnoreCase("Self", ce.getStringCellValue())) ? "Self"
												: "Dolphins";
								shippingDetails.setType(shippingType);
							}
						} else if (subHeaderValue.equals("Pickup Discount available,")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								double pickupDiscount = ce.getNumericCellValue();
								if (pickupDiscount > 0) {
									shippingDetails.setIsPickupDiscountEligible(true);
									shippingDetails.setPickupDiscount(
											new PickupDiscount(BigDecimal.valueOf(ce.getNumericCellValue()).setScale(2,
													RoundingMode.HALF_UP), "INR"));
								} else {
									shippingDetails.setIsPickupDiscountEligible(false);
								}

							}
						} else if (subHeaderValue.equals("Dispatch SLA")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setDispatchSLA((int) ce.getNumericCellValue());
							}
						} else if (subHeaderValue.equals("Time Limit to Return the Product")) {
							if ((ce.getCellType().equals(CellType.NUMERIC))) {
								dolphinSKUED.setReturnPolicy(new ReturnPolicy((int) ce.getNumericCellValue()));
							}
						} else if (subHeaderValue.equals("Yes/No (If 'Yes' What is Service Cost)")) {
							if ((ce.getCellType().equals(CellType.STRING)
									&& StringUtils.isNotBlank(ce.getStringCellValue())
									&& StringUtils.equalsIgnoreCase("No", ce.getStringCellValue()))) {
								dolphinSKUED.setInstallationAndDemoServices(new InstallationAndDemoServices(false, 0));
							} else if (ce.getCellType().equals(CellType.NUMERIC)) {
								dolphinSKUED.setInstallationAndDemoServices(
										new InstallationAndDemoServices(true, (int) ce.getNumericCellValue()));
							}
						}
					}
				}
				excelData.setProductED(productED);
				excelData.setDolphinsVariantsED(dolphinsVariantsED);
				dolphinSKUED.setPrice(price);
				dolphinSKUED.setPackageDetails(packageDetails);
				dolphinSKUED.setShippingDetails(shippingDetails);
				excelData.setDolphinSKUED(dolphinSKUED);
				productDetailsList.add(excelData);
			}
		} catch (Exception e) {
			System.err.println("error msg " + e.getLocalizedMessage());
		}

		return productDetailsList;
	}

	private int findNextColIndex(Row ro, int columnIndex) {
		for (int j = columnIndex + 1; j <= ro.getLastCellNum(); j++) {
			Cell ce = ro.getCell(j);
			if (validateCellStringNumeric(ce)) {
				int aNext = ce.getColumnIndex();
				if (aNext > columnIndex || aNext >= ro.getLastCellNum()) {
					return aNext;
				}
			}
		}
		return ro.getLastCellNum();
	}

	private boolean validateCellStringNumeric(Cell ce) {
		return (ObjectUtils.isNotEmpty(ce)
				&& (ce.getCellType().equals(CellType.STRING) || ce.getCellType().equals(CellType.NUMERIC)));
	}

	private String numericSafeString(Cell ce) {
		return ce.getCellType().equals(CellType.NUMERIC) ? String.valueOf(ce.getNumericCellValue())
				: ce.getStringCellValue();
	}

	private BigDecimal convertDoubleToBigDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
	}

}
