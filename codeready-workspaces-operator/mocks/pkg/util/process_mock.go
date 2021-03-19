// Code generated by MockGen. DO NOT EDIT.
// Source: pkg/util/process.go

// Package mock_util is a generated GoMock package.
package mock_util

import (
	gomock "github.com/golang/mock/gomock"
	reflect "reflect"
)

// MockRunnable is a mock of Runnable interface
type MockRunnable struct {
	ctrl     *gomock.Controller
	recorder *MockRunnableMockRecorder
}

// MockRunnableMockRecorder is the mock recorder for MockRunnable
type MockRunnableMockRecorder struct {
	mock *MockRunnable
}

// NewMockRunnable creates a new mock instance
func NewMockRunnable(ctrl *gomock.Controller) *MockRunnable {
	mock := &MockRunnable{ctrl: ctrl}
	mock.recorder = &MockRunnableMockRecorder{mock}
	return mock
}

// EXPECT returns an object that allows the caller to indicate expected use
func (m *MockRunnable) EXPECT() *MockRunnableMockRecorder {
	return m.recorder
}

// Run mocks base method
func (m *MockRunnable) Run(name string, args ...string) error {
	m.ctrl.T.Helper()
	varargs := []interface{}{name}
	for _, a := range args {
		varargs = append(varargs, a)
	}
	ret := m.ctrl.Call(m, "Run", varargs...)
	ret0, _ := ret[0].(error)
	return ret0
}

// Run indicates an expected call of Run
func (mr *MockRunnableMockRecorder) Run(name interface{}, args ...interface{}) *gomock.Call {
	mr.mock.ctrl.T.Helper()
	varargs := append([]interface{}{name}, args...)
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "Run", reflect.TypeOf((*MockRunnable)(nil).Run), varargs...)
}

// GetStdOut mocks base method
func (m *MockRunnable) GetStdOut() string {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "GetStdOut")
	ret0, _ := ret[0].(string)
	return ret0
}

// GetStdOut indicates an expected call of GetStdOut
func (mr *MockRunnableMockRecorder) GetStdOut() *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "GetStdOut", reflect.TypeOf((*MockRunnable)(nil).GetStdOut))
}

// GetStdErr mocks base method
func (m *MockRunnable) GetStdErr() string {
	m.ctrl.T.Helper()
	ret := m.ctrl.Call(m, "GetStdErr")
	ret0, _ := ret[0].(string)
	return ret0
}

// GetStdErr indicates an expected call of GetStdErr
func (mr *MockRunnableMockRecorder) GetStdErr() *gomock.Call {
	mr.mock.ctrl.T.Helper()
	return mr.mock.ctrl.RecordCallWithMethodType(mr.mock, "GetStdErr", reflect.TypeOf((*MockRunnable)(nil).GetStdErr))
}
