testCountBySqlFile =
SELECT count(*) FROM OPE
WHERE USER_ID  = :userId

findAllBySqlFile =
SELECT
    USER_ID,
    COMPANY_NM,
    DEPARTMENT_NM
FROM
    OPE
WHERE
   USER_ID  = :userId