CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE permissions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code VARCHAR(100) NOT NULL UNIQUE,
  description TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE roles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code VARCHAR(80) NOT NULL UNIQUE,
  name VARCHAR(120) NOT NULL,
  description TEXT,
  system_role BOOLEAN NOT NULL DEFAULT false,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE role_permissions (
  role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
  permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID,
  PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE territories (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  territory_name VARCHAR(160) NOT NULL,
  region VARCHAR(120),
  state VARCHAR(120),
  city VARCHAR(120),
  pin_codes TEXT,
  active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);
CREATE INDEX idx_territories_city ON territories(city);

CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  company_code VARCHAR(80) NOT NULL DEFAULT 'MRX',
  name VARCHAR(160) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  username VARCHAR(120) NOT NULL UNIQUE,
  mobile VARCHAR(40),
  employee_id VARCHAR(80) UNIQUE,
  designation VARCHAR(120),
  password_hash VARCHAR(255) NOT NULL,
  territory_id UUID REFERENCES territories(id),
  reporting_manager_id UUID REFERENCES users(id),
  joining_date DATE,
  enabled BOOLEAN NOT NULL DEFAULT true,
  account_locked BOOLEAN NOT NULL DEFAULT false,
  last_login_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);
CREATE INDEX idx_users_territory ON users(territory_id);

CREATE TABLE user_roles (
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID,
  PRIMARY KEY (user_id, role_id)
);

CREATE TABLE refresh_tokens (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  token_hash VARCHAR(255) NOT NULL UNIQUE,
  expires_at TIMESTAMPTZ NOT NULL,
  revoked_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE password_reset_tokens (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  token_hash VARCHAR(255) NOT NULL UNIQUE,
  expires_at TIMESTAMPTZ NOT NULL,
  used_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE hospitals (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(180) NOT NULL,
  address TEXT,
  contact_person VARCHAR(160),
  phone VARCHAR(40),
  email VARCHAR(180),
  territory_id UUID REFERENCES territories(id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE doctors (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  doctor_name VARCHAR(180) NOT NULL,
  specialty VARCHAR(120),
  qualification VARCHAR(120),
  hospital_id UUID REFERENCES hospitals(id),
  clinic VARCHAR(180),
  address TEXT,
  phone VARCHAR(40),
  email VARCHAR(180),
  category VARCHAR(10),
  potential_score INTEGER NOT NULL DEFAULT 0,
  territory_id UUID REFERENCES territories(id),
  latitude NUMERIC(10, 7),
  longitude NUMERIC(10, 7),
  notes TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);
CREATE INDEX idx_doctors_specialty ON doctors(specialty);
CREATE INDEX idx_doctors_territory ON doctors(territory_id);

CREATE TABLE pharmacies (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  pharmacy_name VARCHAR(180) NOT NULL,
  owner VARCHAR(160),
  address TEXT,
  contact VARCHAR(80),
  territory_id UUID REFERENCES territories(id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE products (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(180) NOT NULL,
  sku VARCHAR(80) UNIQUE,
  active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE visits (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  mr_id UUID NOT NULL REFERENCES users(id),
  doctor_id UUID REFERENCES doctors(id),
  pharmacy_id UUID REFERENCES pharmacies(id),
  visit_date DATE NOT NULL,
  planned_start TIMESTAMPTZ,
  check_in_time TIMESTAMPTZ,
  check_out_time TIMESTAMPTZ,
  check_in_latitude NUMERIC(10, 7),
  check_in_longitude NUMERIC(10, 7),
  check_out_latitude NUMERIC(10, 7),
  check_out_longitude NUMERIC(10, 7),
  distance_from_site_meters NUMERIC(10, 2),
  doctor_met BOOLEAN NOT NULL DEFAULT false,
  discussion_notes TEXT,
  products_discussed TEXT,
  samples_given TEXT,
  photo_url TEXT,
  status VARCHAR(30) NOT NULL DEFAULT 'PLANNED',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID,
  CONSTRAINT chk_visit_status CHECK (status IN ('PLANNED','COMPLETED','MISSED','RESCHEDULED','CANCELLED'))
);
CREATE INDEX idx_visits_mr_date ON visits(mr_id, visit_date);
CREATE INDEX idx_visits_status ON visits(status);

CREATE TABLE attendance (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id),
  attendance_date DATE NOT NULL,
  check_in_time TIMESTAMPTZ,
  check_out_time TIMESTAMPTZ,
  check_in_latitude NUMERIC(10, 7),
  check_in_longitude NUMERIC(10, 7),
  check_out_latitude NUMERIC(10, 7),
  check_out_longitude NUMERIC(10, 7),
  status VARCHAR(30) NOT NULL DEFAULT 'PRESENT',
  working_minutes INTEGER,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID,
  UNIQUE (user_id, attendance_date)
);

CREATE TABLE dcr_reports (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id),
  report_date DATE NOT NULL,
  discussion_summary TEXT,
  samples_given TEXT,
  follow_ups TEXT,
  expenses NUMERIC(12, 2) NOT NULL DEFAULT 0,
  status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
  reviewer_id UUID REFERENCES users(id),
  reviewed_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE tour_plans (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id),
  plan_type VARCHAR(20) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  summary TEXT,
  status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  approver_id UUID REFERENCES users(id),
  approval_notes TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE leave_requests (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id),
  leave_type VARCHAR(60) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  reason TEXT,
  document_url TEXT,
  status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  approver_id UUID REFERENCES users(id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE expenses (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id),
  expense_type VARCHAR(40) NOT NULL,
  expense_date DATE NOT NULL,
  amount NUMERIC(12, 2) NOT NULL,
  receipt_url TEXT,
  notes TEXT,
  status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
  approver_id UUID REFERENCES users(id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE sample_inventory (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  product_id UUID NOT NULL REFERENCES products(id),
  territory_id UUID REFERENCES territories(id),
  quantity INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE sample_distributions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  product_id UUID NOT NULL REFERENCES products(id),
  doctor_id UUID NOT NULL REFERENCES doctors(id),
  user_id UUID NOT NULL REFERENCES users(id),
  quantity INTEGER NOT NULL,
  distributed_on DATE NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE orders (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  pharmacy_id UUID REFERENCES pharmacies(id),
  distributor_name VARCHAR(180),
  user_id UUID REFERENCES users(id),
  order_date DATE NOT NULL,
  amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
  status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE notifications (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id),
  title VARCHAR(180) NOT NULL,
  message TEXT NOT NULL,
  channel VARCHAR(30) NOT NULL DEFAULT 'IN_APP',
  read_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);

CREATE TABLE audit_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id),
  action VARCHAR(120) NOT NULL,
  entity_type VARCHAR(120),
  entity_id UUID,
  ip_address VARCHAR(80),
  details TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID,
  updated_by UUID
);
CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
