CREATE  VIEW PHC2Subj as	
Select 
	public_health_case_uid,
	person_uid,
	diagnosis_date,
	PHC_code,
	PHC_code_short_desc,     
	PHC_code_desc,
	state_cd,
     	state,
	cnty_cd,
	county,
	case_class_cd,
	cd_system_cd,
	cd_system_desc_txt,
	confidentiality_cd,
	confidentiality_desc_txt,
	detection_method_cd,
	detection_method_desc_txt,
	disease_imported_cd,
	disease_imported_desc_txt,
	group_case_cnt,
	investigation_status_cd,
	jurisdiction_cd,
	mmwr_week,
	mmwr_year,
	outbreak_ind,
	outbreak_from_time,
	outbreak_to_time,
	outbreak_name,
	outcome_cd,
	pat_age_at_onset,
	pat_age_at_onset_unit_cd,
	prog_area_cd,
	record_status_cd,
	rpt_cnty_cd,
	rpt_form_cmplt_time,
	rpt_source_cd,
	rpt_source_desc_txt,
	rpt_to_county_time,
	rpt_to_state_time,
	status_cd,
	awareness_cd,
	awareness_desc_txt,
	adults_in_house_nbr,
	age_category_cd,
	age_reported,
	age_reported_time,
	age_reported_unit_cd,
	birth_gender_cd,
	birth_order_nbr,
	birth_time,
	birth_time_calc,
	cd,
	person_code_desc,
	children_in_house_nbr,
	curr_sex_cd,
	deceased_ind_cd,
	deceased_time,
	education_level_cd,
	education_level_desc_txt,
	ethnic_group_ind,
	ethnic_group_ind_desc,
	marital_status_cd,
	marital_status_desc_txt,
	multiple_birth_ind,
	occupation_cd,
	prim_lang_cd,
	prim_lang_desc_txt,
	ELP_from_time,
	ELP_to_time,
	ELP_class_cd class_cd,
	use_cd,
	postal_locator_uid,
	census_block_cd,
	census_minor_civil_division_cd,
	census_track_cd,
	city_cd,
	city_desc_txt,
	cntry_cd,
	region_district_cd,
	MSA_congress_district_cd,
	zip_cd,
	PST_record_status_time,
	PST_record_status_cd
From
	publichealthcasefact
