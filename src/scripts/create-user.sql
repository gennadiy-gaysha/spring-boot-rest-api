-- Drop user first if they exist
DROP USER if exists 'gennadiy_gaysha'@'%' ;

-- Now create user with prop privileges
CREATE USER 'gennadiy_gaysha'@'%' IDENTIFIED BY '123';

GRANT ALL PRIVILEGES ON * . * TO 'gennadiy_gaysha'@'%';