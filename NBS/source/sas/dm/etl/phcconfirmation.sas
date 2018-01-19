/*Confirmation Date*/
proc sql;
	create table confirm as 
	select	public_health_case_uid,
			confirmation_method_time,
			confirmation_method_cd,
			confirmation_method_desc_txt
	from nbs_ods.confirmation_method
	where confirmation_method_time ~= .
	order by public_health_case_uid,confirmation_method_time;
quit;

data Phcconfirmation;
	set	confirm (keep = public_health_case_uid
		confirmation_method_time confirmation_method_cd);
	by 	public_health_case_uid confirmation_method_time;
	if  first.confirmation_method_time then output;
run;
