INSERT INTO permissions (code, description) VALUES
('CREATE_VISIT','Schedule and create visits'),
('START_VISIT','Start geo-tagged visits'),
('END_VISIT','End geo-tagged visits'),
('SUBMIT_DCR','Submit daily call reports'),
('VIEW_ROUTE','View and optimize routes'),
('MANAGE_USERS','Create, edit, activate and disable users'),
('MANAGE_ROLES','Manage roles and permission assignments'),
('MANAGE_TERRITORIES','Manage territories'),
('MANAGE_DOCTORS','Manage doctor master data'),
('MANAGE_HOSPITALS','Manage hospital master data'),
('MANAGE_PHARMACIES','Manage pharmacy master data'),
('APPROVE_VISIT','Approve or validate visits'),
('APPROVE_TOUR_PLAN','Approve tour plans'),
('APPROVE_LEAVE','Approve leave requests'),
('APPROVE_EXPENSE','Approve expense requests'),
('VIEW_REPORTS','View operational reports'),
('VIEW_ANALYTICS','View dashboards and analytics'),
('MANAGE_SAMPLES','Manage sample inventory'),
('MANAGE_ORDERS','Manage pharmacy and distributor orders'),
('VIEW_AUDIT_LOGS','View audit logs')
ON CONFLICT (code) DO NOTHING;

INSERT INTO roles (code, name, description, system_role) VALUES
('ADMIN','Administrator','Platform administrator role assembled from permissions', true),
('USER','Medical Representative','Field medical representative role assembled from permissions', true)
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.code = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.code IN
('CREATE_VISIT','START_VISIT','END_VISIT','SUBMIT_DCR','VIEW_ROUTE','MANAGE_DOCTORS','VIEW_REPORTS','VIEW_ANALYTICS')
WHERE r.code = 'USER'
ON CONFLICT DO NOTHING;

INSERT INTO territories (territory_name, region, state, city, pin_codes)
VALUES ('Mumbai Central Pharma Belt','West','Maharashtra','Mumbai','400001,400002,400003')
ON CONFLICT DO NOTHING;

INSERT INTO users (company_code, name, email, username, mobile, employee_id, designation, password_hash, territory_id, joining_date)
SELECT 'MRX','Admin User','admin@mrsystem.local','admin','9999999999','ADM-001','System Administrator',
'$2a$10$M0cZ6zYTnYYZ1tfhnUmt0OXj5uVjkmNdkjkhdl6faybDUBh.qZMju', t.id, CURRENT_DATE
FROM territories t WHERE t.territory_name = 'Mumbai Central Pharma Belt'
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (company_code, name, email, username, mobile, employee_id, designation, password_hash, territory_id, reporting_manager_id, joining_date)
SELECT 'MRX','Sample MR','mr@mrsystem.local','mr','8888888888','MR-001','Medical Representative',
'$2a$10$M0cZ6zYTnYYZ1tfhnUmt0OXj5uVjkmNdkjkhdl6faybDUBh.qZMju', t.id, admin.id, CURRENT_DATE
FROM territories t, users admin
WHERE t.territory_name = 'Mumbai Central Pharma Belt' AND admin.username = 'admin'
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.code = 'ADMIN' WHERE u.username = 'admin'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.code = 'USER' WHERE u.username = 'mr'
ON CONFLICT DO NOTHING;

INSERT INTO hospitals (name, address, contact_person, phone, email, territory_id)
SELECT 'City General Hospital','Fort, Mumbai','Dr. Mehra','022-40000000','contact@citygeneral.example', id
FROM territories WHERE territory_name = 'Mumbai Central Pharma Belt'
ON CONFLICT DO NOTHING;

INSERT INTO doctors (doctor_name, specialty, qualification, hospital_id, clinic, address, phone, email, category, potential_score, territory_id, latitude, longitude)
SELECT 'Dr. Asha Mehta','Cardiology','MD', h.id, 'Mehta Heart Clinic', 'Fort, Mumbai', '9876543210',
'asha.mehta@example.com', 'A', 92, h.territory_id, 18.9388, 72.8354
FROM hospitals h WHERE h.name = 'City General Hospital'
ON CONFLICT DO NOTHING;

INSERT INTO pharmacies (pharmacy_name, owner, address, contact, territory_id)
SELECT 'Central Care Pharmacy','Rohan Shah','Fort, Mumbai','9876500000', id
FROM territories WHERE territory_name = 'Mumbai Central Pharma Belt'
ON CONFLICT DO NOTHING;

INSERT INTO products (name, sku) VALUES
('CardioPlus 10mg','CARDIOPLUS-10'),
('GlucoBalance','GLUCO-BAL')
ON CONFLICT (sku) DO NOTHING;

INSERT INTO visits (mr_id, doctor_id, visit_date, planned_start, status, discussion_notes)
SELECT mr.id, d.id, CURRENT_DATE, now() + interval '2 hours', 'PLANNED', 'Monthly relationship visit'
FROM users mr, doctors d WHERE mr.username = 'mr' AND d.doctor_name = 'Dr. Asha Mehta'
ON CONFLICT DO NOTHING;
