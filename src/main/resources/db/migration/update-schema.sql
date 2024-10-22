CREATE TABLE alphafrog_index_info
(
    index_info_id BIGINT       NOT NULL,
    ts_code       VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    fullname      VARCHAR(255),
    market        VARCHAR(255) NOT NULL,
    publisher     VARCHAR(255),
    index_type    VARCHAR(255),
    category      VARCHAR(255),
    base_date     BIGINT,
    base_point    DOUBLE PRECISION,
    list_date     BIGINT,
    weight_rule   VARCHAR(255),
    "desc"        VARCHAR(255),
    exp_date      BIGINT,
    CONSTRAINT pk_alphafrog_index_info PRIMARY KEY (index_info_id)
);

ALTER TABLE alphafrog_index_info
    ADD CONSTRAINT uc_aad697656764315004b3baae1 UNIQUE (ts_code);