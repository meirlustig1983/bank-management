.PHONY: install_minikube install_kubernetes_cli kubectl_version minikube_start minikube_stop minikube_update minikube_delete minikube_init minikube_restart bank_management_service_ip_dev postgres_service_ip_dev prometheus_service_ip_dev grafana_service_ip_dev create_namespaces delete_namespaces get_namespaces get_nodes get_pods_dev get_services_dev get_secrets_dev get_ingress_dev get_configmaps get_configmaps_dev create_postgres_dev delete_postgres_dev create_bank_management_dev delete_bank_management_dev create_prometheus_dev delete_prometheus_dev create_grafana_dev delete_grafana_dev create_ingress_dev delete_ingress_dev init

install_minikube:
	brew install minikube || true

install_kubernetes_cli:
	brew install kubernetes-cli || true

kubectl_version:
	kubectl version

minikube_start: install_minikube install_kubernetes_cli
	minikube start

minikube_stop:
	minikube stop

minikube_delete:
	minikube delete

minikube_update:
	minikube update-check

minikube_init: minikube_start minikube_update create_namespaces
	minikube addons enable ingress
	minikube addons enable ingress-dns

minikube_restart: minikube_delete minikube_init

bank_management_service_ip_dev:
	minikube service bank-management --url -n dev

postgres_service_ip_dev:
	minikube service postgres --url -n dev

prometheus_service_ip_dev:
	minikube service prometheus --url -n dev

grafana_service_ip_dev:
	minikube service grafana --url -n dev

create_secret_for_docker:
	kubectl create secret generic dockerhub-secret-temp \
		--from-file=.dockerconfigjson=config.json \
		--type=kubernetes.io/dockerconfigjson

get_nodes:
	kubectl get nodes

get_pods_dev:
	kubectl get pods -n dev

get_secrets_dev:
	kubectl get secrets -n dev

get_services_dev:
	kubectl get services -n dev

get_ingress_dev:
	kubectl get ingress -n dev

create_namespaces:
	kubectl apply -f namespace.yaml

delete_namespaces:
	kubectl delete -f namespace.yaml

get_namespaces:
	kubectl get namespaces

get_configmaps:
	kubectl get configmaps --all-namespaces

get_configmaps_dev:
	kubectl get configmaps -n dev

create_postgres_dev:
	kubectl apply -f postgres-deployment.yaml -n dev

delete_postgres_dev:
	kubectl delete -f postgres-deployment.yaml -n dev

create_bank_management_dev: create_postgres_dev
	kubectl apply -f bank-management-deployment.yaml -n dev

delete_bank_management_dev:
	kubectl delete -f bank-management-deployment.yaml -n dev

create_prometheus_dev:
	kubectl apply -f prometheus-deployment.yaml -n dev

delete_prometheus_dev:
	kubectl delete -f prometheus-deployment.yaml -n dev

create_grafana_dev:
	kubectl apply -f grafana-deployment.yaml -n dev

delete_grafana_dev:
	kubectl delete -f grafana-deployment.yaml -n dev

create_ingress_dev:
	kubectl apply -f ingress-deployment.yaml -n dev

delete_ingress_dev:
	kubectl delete -f ingress-deployment.yaml -n dev

init: create_namespaces create_postgres_dev create_bank_management_dev create_prometheus_dev create_grafana_dev
