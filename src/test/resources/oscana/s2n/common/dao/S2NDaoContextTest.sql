testFindAllBySqlFile01 =
SELECT
    *
FROM
    PROJECT
WHERE
   PROJECT_ID  = :projectId

testFindAllBySqlFile02 =
SELECT
    *
FROM
    PROJECT

testFindBySqlFile =
SELECT
    *
FROM
    PROJECT
WHERE
   PROJECT_ID  = :projectId

testCountBySqlFile =
SELECT count(*) FROM PROJECT
WHERE
PROJECT_ID  = :projectId